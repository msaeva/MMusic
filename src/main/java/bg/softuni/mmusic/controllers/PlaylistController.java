package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.constants.Authorities;
import bg.softuni.mmusic.model.dtos.playlist.CreatePlaylistDto;
import bg.softuni.mmusic.model.dtos.playlist.PublicSimplePlaylistDto;
import bg.softuni.mmusic.model.dtos.song.SongDto;
import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.services.AuthService;
import bg.softuni.mmusic.services.PlaylistService;
import bg.softuni.mmusic.services.PlaylistsSongService;
import bg.softuni.mmusic.services.SongService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("playlist")
@Slf4j
public class PlaylistController {

    private final PlaylistService playlistService;
    private final AuthService authService;
    private final SongService songService;
    private final PlaylistsSongService playlistsSongService;

    @Autowired
    public PlaylistController(PlaylistService playlistService,
                              AuthService authService,
                              SongService songService,
                              PlaylistsSongService playlistsSongService) {

        this.playlistService = playlistService;
        this.authService = authService;
        this.songService = songService;
        this.playlistsSongService = playlistsSongService;
    }

    @ModelAttribute(name = "createPlaylistDto")
    public CreatePlaylistDto createPlaylistDto() {
        return new CreatePlaylistDto();
    }

    @GetMapping("/create")
    public String create() {
        return "create-playlist";
    }

    @Secured(Authorities.MUSICIAN)
    @PostMapping("/create")
    public String create(@Valid CreatePlaylistDto createPlaylistDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasFieldErrors()) {
            redirectAttributes.addFlashAttribute("createPlaylistDto", createPlaylistDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createPlaylistDto", bindingResult);

            return "redirect:/playlist/create";
        }

        Playlist playlist = playlistService.create(createPlaylistDto);
        log.info("Created new playlist: " + playlist);

        return "redirect:/playlist/" + playlist.getUuid();
    }

    @GetMapping("/{uuid}")
    public ModelAndView getPlaylist(@PathVariable(name = "uuid") String playlistUuid, ModelAndView modelAndView) {
        User authUser = authService.getAuthenticatedUser();

        Playlist playlist = playlistService.findByUuid(playlistUuid);
        PublicSimplePlaylistDto playlistDto = playlistService.publicSimplePlaylistDto(playlist);
        List<SongDto> songsToAddDto = songService.findAllPublicToAddToPlaylist(playlistUuid);

        modelAndView.setViewName("single-page-playlist");
        modelAndView.addObject("isOwner", playlistService.checkIsOwner(playlist, authUser));
        modelAndView.addObject("songsToAdd", songsToAddDto);
        modelAndView.addObject("playlist", playlistDto);
        return modelAndView;
    }

    @PostMapping("/{uuid}/add/{songUuid}")
    @ResponseBody
    public HttpStatus addSongToPlaylist(@PathVariable(name = "uuid") String playlistUuid,
                                        @PathVariable(name = "songUuid") String songUuid) {
        try {
            playlistService.addSongToPlaylist(songUuid, playlistUuid);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }

    @DeleteMapping("/{uuid}/song/{songUuid}")
    @ResponseBody
    public HttpStatus deleteSongFromPlaylist(@PathVariable(name = "uuid") String playlistUuid,
                                             @PathVariable(name = "songUuid") String songUuid) {
        try {
            playlistsSongService.removeSong(playlistUuid, songUuid);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }
}
