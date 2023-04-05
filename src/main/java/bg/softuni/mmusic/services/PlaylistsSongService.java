package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.entities.PlaylistSongs;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.repositories.PlaylistRepository;
import bg.softuni.mmusic.repositories.PlaylistSongsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PlaylistsSongService {
    private final PlaylistSongsRepository playlistSongsRepository;
    private final PlaylistRepository playlistRepository;
    private final SongService songService;

    @Autowired
    public PlaylistsSongService(PlaylistSongsRepository playlistSongsRepository, PlaylistRepository playlistRepository, SongService songService) {
        this.playlistSongsRepository = playlistSongsRepository;
        this.playlistRepository = playlistRepository;
        this.songService = songService;
    }

    public void removeSong(String playlistUuid, String songUuid) {

        Playlist playlist = playlistRepository.findByUuid(playlistUuid).orElseThrow(NoSuchElementException::new);
        Song songToRemove = songService.findSongByUuid(songUuid);

        PlaylistSongs playlistSongs =
                playlistSongsRepository.getByPlaylistUuidAndSongUuid(playlist.getUuid(), songToRemove.getUuid());

        playlist.getSongs().remove(songToRemove);
        playlistSongsRepository.delete(playlistSongs);
    }
}
