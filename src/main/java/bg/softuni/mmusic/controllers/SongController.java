package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.constants.Authorities;
import bg.softuni.mmusic.controllers.validations.SearchSongValidation;
import bg.softuni.mmusic.model.dtos.song.AddSongDto;
import bg.softuni.mmusic.model.dtos.song.PublicDetailedSongDto;
import bg.softuni.mmusic.model.dtos.song.PublicSimpleSongDto;
import bg.softuni.mmusic.model.dtos.song.UpdateSongDto;
import bg.softuni.mmusic.model.entities.Song;
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

@Controller
@Slf4j
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

        Pagination<List<PublicSimpleSongDto>> pageableDto =
                new Pagination<>(validation.getCount(), validation.getOffset(), songs.getTotalElements());

        List<PublicSimpleSongDto> publicSimpleSongDtos =
                songService.toPublicSimpleSongDto(songs);
        pageableDto.setData(publicSimpleSongDtos);

        modelAndView.addObject("pagination", pageableDto);
        modelAndView.setViewName("search");

        return modelAndView;

    }

    @GetMapping("/{uuid}/view")
    public ModelAndView viewSong(@PathVariable(name = "uuid") String uuid, ModelAndView modelAndView) {

        Song song = songService.findSongByUuid(uuid);
        PublicDetailedSongDto publicDetailedSongDto = songService.toDetailedSongDto(song);

        modelAndView.setViewName("single-page-song");
        modelAndView.addObject("song", publicDetailedSongDto);

        return modelAndView;
    }
}
