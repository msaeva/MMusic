package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.constants.Authorities;
import bg.softuni.mmusic.controllers.validations.SearchSongValidation;
import bg.softuni.mmusic.model.dtos.song.AddSongDto;
import bg.softuni.mmusic.model.dtos.song.PublicDetailedSongDto;
import bg.softuni.mmusic.model.dtos.song.SongDto;
import bg.softuni.mmusic.model.dtos.song.UpdateSongDto;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.model.error.SongNotFoundException;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.repositories.SongRepository;
import bg.softuni.mmusic.repositories.StyleRepository;
import bg.softuni.mmusic.repositories.UserFavouriteSongsRepository;
import bg.softuni.mmusic.repositories.UserLikedSongsRepository;
import bg.softuni.mmusic.services.AuthService;
import bg.softuni.mmusic.services.Pagination;
import bg.softuni.mmusic.services.SongService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final SongRepository songRepository;

    private final StyleRepository styleRepository;

    @Autowired
    public SongController(SongService songService,
                          AuthService authService,
                          UserFavouriteSongsRepository favouriteSongsRepository,
                          UserLikedSongsRepository userLikedSongsRepository,
                          SongMapper songMapper, SongRepository songRepository, StyleRepository styleRepository) {
        this.songService = songService;
        this.authService = authService;
        this.favouriteSongsRepository = favouriteSongsRepository;
        this.userLikedSongsRepository = userLikedSongsRepository;
        this.songMapper = songMapper;
        this.songRepository = songRepository;
        this.styleRepository = styleRepository;
    }

    @ModelAttribute(name = "addSongDto")
    public AddSongDto addSongDto() {
        return new AddSongDto();
    }

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

        Song song = songService.addSong(addSongDto);

        return "redirect:/song/" + song.getUuid();
    }

    @GetMapping("/{uuid}/update")
    public ModelAndView update(@PathVariable(name = "uuid") String uuid, ModelAndView modelAndView) {
        Song songToUpdate = songService.findSongByUuid(uuid);

        modelAndView.setViewName("update-song");

        if (authService.getAuthenticatedUser()
                .getOwnSongs().stream()
                .noneMatch(song -> song.getUuid().equals(songToUpdate.getUuid()))) {

            throw new SongNotFoundException(songToUpdate.getUuid());
        }

        UpdateSongDto songDto = songService.toUpdateSongDto(songToUpdate);
        modelAndView.addObject("songDto", songDto);

        return modelAndView;
    }

    @PutMapping("/{uuid}/update")
    public String putUpdate(@PathVariable(name = "uuid") String uuid, @Valid UpdateSongDto updateSongDto) {

        Song songToUpdate = songService.findSongByUuid(uuid);

        if (authService.getAuthenticatedUser()
                .getOwnSongs().stream()
                .noneMatch(song -> song.getUuid().equals(songToUpdate.getUuid()))) {

            throw new SongNotFoundException(songToUpdate.getUuid());
        }
        songService.update(songToUpdate, updateSongDto);

        return "redirect:/song/" + uuid;
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

        Pagination<List<SongDto>> pageableDto =
                new Pagination<>(validation.getCount(), validation.getOffset(), songs.getTotalElements());


        List<SongDto> songDtos =
                songs.stream().map(songMapper::toSongDto).collect(Collectors.toList());
        pageableDto.setData(songDtos);

        modelAndView.addObject("pagination", pageableDto);
        modelAndView.setViewName("search");

        return modelAndView;

    }

    @GetMapping("/{uuid}")
    public ModelAndView getSong(@PathVariable(name = "uuid") String uuid, ModelAndView modelAndView) {
        User authUser = authService.getAuthenticatedUser();

        Song song = songService.findSongByUuid(uuid);

        boolean liked = false;
        boolean favourite = false;
        boolean displayButtons = false;
        if (authUser != null) {
            if (authUser.getOwnSongs().stream().noneMatch(s -> s.getUuid().equals(song.getUuid())) &&
                    authUser.getRoles().stream().noneMatch(role -> role.getRole().equals(Role.ADMIN))) {
                displayButtons = true;
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
        modelAndView.addObject("displayButtons", displayButtons);
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
