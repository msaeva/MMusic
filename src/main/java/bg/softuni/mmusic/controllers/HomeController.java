package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.controllers.validations.SearchSongValidation;
import bg.softuni.mmusic.model.dtos.PublicSimpleSongDto;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.services.Pagination;
import bg.softuni.mmusic.services.SongService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@Slf4j
public class HomeController {
    private final SongService songService;
    private final SongMapper songMapper;

    public HomeController(SongService songService, SongMapper songMapper) {
        this.songService = songService;
        this.songMapper = songMapper;
    }

    @GetMapping("/")
    public ModelAndView getHome(ModelAndView modelAndView, @Valid SearchSongValidation validation) {

        Page<Song> songs = songService.getAll(validation);

        Pagination<List<PublicSimpleSongDto>> pageableDto =
                new Pagination<>(validation.getCount(), validation.getOffset(), songs.getTotalElements());

        pageableDto.setData(songs.stream().map(songMapper::toPublicSimpleSongDto).toList());

        modelAndView.setViewName("index");
        modelAndView.addObject("songs", pageableDto);

        return modelAndView;
    }
}
