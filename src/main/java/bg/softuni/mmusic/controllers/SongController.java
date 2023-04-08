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
import bg.softuni.mmusic.services.*;
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
    private final UserFavouriteSongsService userFavouriteSongsService;
    private final UserLikedSongsService userLikedSongsService;

    private final SongMapper songMapper;

    @Autowired
    public SongController(SongService songService,
                          AuthService authService,
                          UserFavouriteSongsService userFavouriteSongsService,
                          UserLikedSongsService userLikedSongsService,
                          SongMapper songMapper) {
        this.songService = songService;
        this.authService = authService;
        this.userFavouriteSongsService = userFavouriteSongsService;
        this.userLikedSongsService = userLikedSongsService;
        this.songMapper = songMapper;

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
        log.info("Added new song : " + song);

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
    /**
     * { @code PUT /song }: update song by its uuid (specified in the request as params)
     * @return redirect to single-page-song
     * */
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

    /**
     * { @code DELETE /song }: delete song by its uuid (specified in the request as params)
     *
     */
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
    /**
     * { @code GET /song }: retrieves song by its uuid (specified in the request as params)
     * @return single-page-song
     * */
    @GetMapping("/{uuid}")
    public ModelAndView getSong(@PathVariable(name = "uuid") String uuid, ModelAndView modelAndView) {
        User authUser = authService.getAuthenticatedUser();

        Song song = songService.findSongByUuid(uuid);

        boolean isLikedByAuthUser = false;
        boolean favourite = false;
        boolean displayButtons = false;


        if (authUser != null) {
            // user cannot like its own songs and admin also
            if (authUser.getOwnSongs().stream().noneMatch(s -> s.getUuid().equals(song.getUuid())) &&
                    authUser.getRoles().stream().noneMatch(role -> role.getRole().equals(Role.ADMIN))) {
                displayButtons = true;
            }

            List<String> userLikedSongsUuids = userLikedSongsService.getUserLikedSongs(authUser.getUuid());
            if (userLikedSongsUuids.stream().anyMatch(songUuid -> songUuid.equals(song.getUuid()))) {
                isLikedByAuthUser = true;
            }
            List<String> userFavouriteSongUuids = userFavouriteSongsService.getUserFavouriteSongs(authUser.getUuid());
            if (userFavouriteSongUuids.stream().anyMatch(songUuid -> songUuid.equals(song.getUuid()))) {
                favourite = true;
            }
        }

        PublicDetailedSongDto publicDetailedSongDto = songService.toDetailedSongDto(song);

        modelAndView.setViewName("single-page-song");
        modelAndView.addObject("displayButtons", displayButtons);
        modelAndView.addObject("liked", isLikedByAuthUser);
        modelAndView.addObject("favourite", favourite);
        modelAndView.addObject("song", publicDetailedSongDto);

        return modelAndView;
    }
    /**
     * { @code POST / like song }: like song by its uuid (specified in the request as params)
     * @return http status
     * */
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
    /**
     * { @code DELETE / unlike song }: unlike song by its uuid (specified in the request as params)
     * @return http status
     * */
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
