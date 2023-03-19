package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.constants.Authorities;
import bg.softuni.mmusic.model.dtos.playlist.CreatePlaylistDto;
import bg.softuni.mmusic.services.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
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
    public String create(@Valid CreatePlaylistDto createPlaylistDto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasFieldErrors()) {
            redirectAttributes.addFlashAttribute("createPlaylistDto", createPlaylistDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createPlaylistDto",
                    bindingResult);

            System.out.println("before redirect");
            return "redirect:/playlist/create";
        }

        playlistService.create(createPlaylistDto);

        return "/index";
    }

    public String addSongToPlaylist(@PathVariable String songUuid, String playlistUuid){
        playlistService.addSongToPlaylist(songUuid, playlistUuid);
        return  null;
    }
}
