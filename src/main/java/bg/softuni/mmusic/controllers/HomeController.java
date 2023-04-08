package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.dtos.song.SongDto;
import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.services.PlaylistService;
import bg.softuni.mmusic.services.SongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Autowired
    public HomeController(SongService songService,
                          SongMapper songMapper,
                          PlaylistService playlistService) {
        this.songService = songService;
        this.songMapper = songMapper;
        this.playlistService = playlistService;
    }

    /**
     * { @code get /home }: get top playlists and most liked songs
     */
    @GetMapping("/")
    public ModelAndView getHome(ModelAndView modelAndView) {
        HashMap<Playlist, Integer> topPlaylists = playlistService.getTopPlaylists();

        List<SongDto> mostLikedSongs = songService
                .getMostLikedSongs(PageRequest.of(0, 5, Sort.Direction.DESC, "likes"))
                .stream().map(songMapper::toSongDto).toList();

        List<SongDto> secondBatchMostLikedSongs = songService
                .getMostLikedSongs(PageRequest.of(1, 5, Sort.Direction.DESC, "likes"))
                .stream().limit(3).map(songMapper::toSongDto).toList();

        modelAndView.setViewName("index");
        modelAndView.addObject("topPlaylists", topPlaylists);
        modelAndView.addObject("mostLikedSongs", mostLikedSongs);
        modelAndView.addObject("moreSongs", secondBatchMostLikedSongs);

        return modelAndView;
    }
}
