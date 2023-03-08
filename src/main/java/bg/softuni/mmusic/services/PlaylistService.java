package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.CreatePlaylistDto;
import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.repositories.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PlaylistService {
    private final AuthService authService;
    private final PlaylistRepository playlistRepository;

    private final SongService songService;

    public PlaylistService(AuthService authService, PlaylistRepository playlistRepository, SongService songService) {
        this.authService = authService;
        this.playlistRepository = playlistRepository;
        this.songService = songService;
    }

    public void create(CreatePlaylistDto createPlaylistDto) {
        User authUser = getAuthUser();
        Playlist newPlaylist = new Playlist();
        newPlaylist.setName(createPlaylistDto.getName());
        newPlaylist.setOwner(authUser);
        newPlaylist.setStatus(createPlaylistDto.getStatus());

        playlistRepository.saveAndFlush(newPlaylist);
    }

    public void addSongToPlaylist(String songUuid, String playlistUuid) {
        User authUser = getAuthUser();
        Playlist playlist = playlistRepository.findById(playlistUuid).orElse(null);
        Song songToAdd = songService.findSongByUuid(songUuid).orElseThrow(NoSuchElementException::new);

        if (playlist != null) {
            playlist.getSongs().add(songToAdd);
            playlistRepository.saveAndFlush(playlist);
        }

    }

    private User getAuthUser() {
        return authService.getAuthenticatedUser();
    }
}
