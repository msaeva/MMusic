package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.constants.Authorities;
import bg.softuni.mmusic.model.dtos.AddSongDto;
import bg.softuni.mmusic.model.dtos.SongDto;
import bg.softuni.mmusic.model.dtos.UpdateSongDto;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.services.AuthService;
import bg.softuni.mmusic.services.SongService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("song")
public class SongController {

    private final SongService songService;
    private final AuthService authService;

    public SongController(SongService songService, AuthService authService) {
        this.songService = songService;
        this.authService = authService;
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
    public String update(@PathVariable(name = "uuid") String uuid, Model model) {
        Song songToUpdate = songService.findSongByUuid(uuid)
                .orElseThrow(NoSuchElementException::new);

        if (authService.getAuthenticatedUser()
                .getOwnSongs().stream()
                .noneMatch(song -> song.getUuid().equals(songToUpdate.getUuid()))) {
        }

        SongDto songDto = songService.toSongDto(songToUpdate);
        model.addAttribute("songDto", songDto);

        return "update-song";
    }

    @PutMapping("/{uuid}/update")
    public String putUpdate(@PathVariable(name = "uuid") String uuid, @Valid UpdateSongDto updateSongDto) {
        Song songToUpdate = songService.findSongByUuid(uuid)
                .orElseThrow(NoSuchElementException::new);

        if (authService.getAuthenticatedUser()
                .getOwnSongs().stream()
                .noneMatch(song -> song.getUuid().equals(songToUpdate.getUuid()))) {

            // TODO return error
        }
        songService.update(songToUpdate, updateSongDto);

        return "/index";
    }

}
