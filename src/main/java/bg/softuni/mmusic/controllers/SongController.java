package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.constants.Authorities;
import bg.softuni.mmusic.model.dtos.AddSongDto;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.services.SongService;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("song")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
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

        return "redirect:song/add";
    }
}
