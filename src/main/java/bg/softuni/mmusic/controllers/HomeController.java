package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.controllers.validations.PublicSongValidation;
import bg.softuni.mmusic.model.dtos.song.SongDto;
import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.repositories.PlaylistRepository;
import bg.softuni.mmusic.services.Pagination;
import bg.softuni.mmusic.services.PlaylistService;
import bg.softuni.mmusic.services.SongService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

@Controller
@Slf4j
public class HomeController {
    private final SongService songService;
    private final SongMapper songMapper;
    private final PlaylistService playlistService;
    private final PlaylistRepository playlistRepository;

    @Autowired
    public HomeController(SongService songService, SongMapper songMapper, PlaylistService playlistService, PlaylistRepository playlistRepository) {
        this.songService = songService;
        this.songMapper = songMapper;
        this.playlistService = playlistService;
        this.playlistRepository = playlistRepository;
    }

    @GetMapping("/")
    public ModelAndView getHome(ModelAndView modelAndView, @Valid PublicSongValidation validation) {

        Page<Song> songs = songService.getMostLikedSongs(validation);

        Pagination<List<SongDto>> pageableDto =
                new Pagination<>(validation.getCount(), validation.getOffset(), songs.getTotalElements());

        pageableDto.setData(songs.stream().map(songMapper::toSongDto).toList());

        HashMap<Playlist, Integer> topPlaylists = playlistService.getTopPlaylists();

        modelAndView.setViewName("index");
        modelAndView.addObject("topPlaylists", topPlaylists);
        modelAndView.addObject("pagination", pageableDto);

        return modelAndView;
    }
}
