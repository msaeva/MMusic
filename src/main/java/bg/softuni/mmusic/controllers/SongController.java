package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.constants.Authorities;
import bg.softuni.mmusic.controllers.validations.SearchSongValidation;
import bg.softuni.mmusic.model.dtos.song.*;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.repositories.PictureRepository;
import bg.softuni.mmusic.repositories.SongRepository;
import bg.softuni.mmusic.repositories.UserFavouriteSongsRepository;
import bg.softuni.mmusic.repositories.UserLikedSongsRepository;
import bg.softuni.mmusic.services.AuthService;
import bg.softuni.mmusic.services.Pagination;
import bg.softuni.mmusic.services.SongService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("song")
public class SongController {

    private final SongService songService;
    private final AuthService authService;
    private final UserFavouriteSongsRepository favouriteSongsRepository;
    private final UserLikedSongsRepository userLikedSongsRepository;

    private final SongMapper songMapper;

    public SongController(SongService songService,
                          AuthService authService,
                          UserFavouriteSongsRepository favouriteSongsRepository,
                          UserLikedSongsRepository userLikedSongsRepository,
                          SongMapper songMapper) {
        this.songService = songService;
        this.authService = authService;
        this.favouriteSongsRepository = favouriteSongsRepository;
        this.userLikedSongsRepository = userLikedSongsRepository;
        this.songMapper = songMapper;
    }

    @ModelAttribute(name = "addSongDto")
    public AddSongDto addSongDto() {
        return new AddSongDto();
    }

    //  @PreAuthorize("hasRole('MUSICIAN')")
    @Secured(Authorities.MUSICIAN)
    @GetMapping("/add")
    public String addSong() {
        return "add-song";
    }

    @Secured(Authorities.MUSICIAN)
    @PostMapping("/add")
    public String addSong(@Valid AddSongDto addSongDto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasFieldErrors()) {
            redirectAttributes.addFlashAttribute("addSongDto", addSongDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addSongDto",
                    bindingResult);
            return "redirect:/song/add";
        }

        songService.addSong(addSongDto);

        return "redirect:/";
    }

    @GetMapping("/{uuid}/update")
    public ModelAndView update(@PathVariable(name = "uuid") String uuid, ModelAndView modelAndView) {
        Song songToUpdate = songService.findSongByUuid(uuid);

        modelAndView.setViewName("update-song");

        if (authService.getAuthenticatedUser()
                .getOwnSongs().stream()
                .noneMatch(song -> song.getUuid().equals(songToUpdate.getUuid()))) {

            // TODO return error
        }

        UpdateSongDto songDto = songService.toUpdateSongDto(songToUpdate);
        songService.update(songToUpdate, songDto);
        modelAndView.addObject("songDto", songDto);

        return modelAndView;
    }

    @PutMapping("/{uuid}/update")
    public String putUpdate(@PathVariable(name = "uuid") String uuid, @Valid UpdateSongDto updateSongDto) {

        Song songToUpdate = songService.findSongByUuid(uuid);

        if (authService.getAuthenticatedUser()
                .getOwnSongs().stream()
                .noneMatch(song -> song.getUuid().equals(songToUpdate.getUuid()))) {

            // TODO return error
        }
        songService.update(songToUpdate, updateSongDto);

        return "/index";
    }

    @DeleteMapping("/{uuid}/delete")
    @ResponseBody
    public HttpStatus deleteSong(@PathVariable(name = "uuid") String uuid) {
        try {
            songService.delete(uuid);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }

    @GetMapping("/search")
    public ModelAndView searchSong(@Valid SearchSongValidation validation, ModelAndView modelAndView) {
        Page<Song> songs = songService.getAll(validation);

        Pagination<List<SearchSongDto>> pageableDto =
                new Pagination<>(validation.getCount(), validation.getOffset(), songs.getTotalElements());


        List<SearchSongDto> searchSongDtos =
                songs.stream().map(songMapper::toSearchSongDto).collect(Collectors.toList());
        pageableDto.setData(searchSongDtos);

        modelAndView.addObject("pagination", pageableDto);
        modelAndView.setViewName("search");

        return modelAndView;

    }

    @GetMapping("/{uuid}/view")
    public ModelAndView viewSong(@PathVariable(name = "uuid") String uuid, ModelAndView modelAndView) {
        User authUser = authService.getAuthenticatedUser();

        Song song = songService.findSongByUuid(uuid);

        boolean liked = false;
        boolean favourite = false;
        boolean own = false;
        if (authUser != null) {
            if (authUser.getOwnSongs().stream().anyMatch(s -> s.getUuid().equals(song.getUuid()))) {
                own = true;
            }
            List<String> userLikedSongsUuids = userLikedSongsRepository.getUserLikedSongs(authUser.getUuid());
            if (userLikedSongsUuids.stream().anyMatch(songUuid -> songUuid.equals(song.getUuid()))) {
                liked = true;
            }
            List<String> userFavouriteSongUuids = favouriteSongsRepository.getUserFavouriteSongs(authUser.getUuid());
            if (userFavouriteSongUuids.stream().anyMatch(songUuid -> songUuid.equals(song.getUuid()))) {
                favourite = true;
            }
        }

        PublicDetailedSongDto publicDetailedSongDto = songService.toDetailedSongDto(song);

        modelAndView.setViewName("single-page-song");
        modelAndView.addObject("own", own);
        modelAndView.addObject("liked", liked);
        modelAndView.addObject("favourite", favourite);
        modelAndView.addObject("song", publicDetailedSongDto);

        return modelAndView;
    }

    @PostMapping("/{uuid}/like")
    @ResponseBody
    public HttpStatus likeSong(@PathVariable(name = "uuid") String uuid) {
        try {
            songService.like(uuid);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }

    @DeleteMapping("/{uuid}/unlike")
    @ResponseBody
    public HttpStatus unlikeSong(@PathVariable(name = "uuid") String uuid) {
        try {
            songService.unlike(uuid);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }

    @PostMapping("/{uuid}/addToFavourite")
    @ResponseBody
    public HttpStatus addToFavourite(@PathVariable(name = "uuid") String uuid) {
        User authUser = authService.getAuthenticatedUser();
        try {
            songService.addToFavourite(uuid, authUser);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }

    @DeleteMapping("/{uuid}/removeFromFavourite")
    @ResponseBody
    public HttpStatus removeFromFavourite(@PathVariable(name = "uuid") String uuid) {
        User authUser = authService.getAuthenticatedUser();
        try {
            songService.removeFromFavourite(uuid, authUser);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }
}
