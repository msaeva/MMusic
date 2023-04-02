package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.playlist.CreatePlaylistDto;
import bg.softuni.mmusic.model.dtos.playlist.PublicSimplePlaylistDto;
import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.entities.PlaylistSongs;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.repositories.PlaylistRepository;
import bg.softuni.mmusic.repositories.PlaylistSongsRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PlaylistService {
    private final AuthService authService;
    private final PlaylistRepository playlistRepository;
    private final SongService songService;
    private final PlaylistSongsRepository playlistSongsRepository;
    private final SongMapper songMapper;


    public PlaylistService(AuthService authService, PlaylistRepository playlistRepository,
                           SongService songService, PlaylistSongsRepository playlistSongsRepository, SongMapper songMapper) {
        this.authService = authService;
        this.playlistRepository = playlistRepository;
        this.songService = songService;
        this.playlistSongsRepository = playlistSongsRepository;
        this.songMapper = songMapper;
    }

    public Playlist create(CreatePlaylistDto createPlaylistDto) {
        User authUser = getAuthUser();
        Playlist newPlaylist = new Playlist();
        newPlaylist.setName(createPlaylistDto.getName());
        newPlaylist.setOwner(authUser);
        newPlaylist.setStatus(createPlaylistDto.getStatus());

        playlistRepository.saveAndFlush(newPlaylist);
        return newPlaylist;
    }

    public void addSongToPlaylist(String songUuid, String playlistUuid) {
        Playlist playlist = playlistRepository.findByUuid(playlistUuid).orElseThrow(NoSuchElementException::new);
        Song songToAdd = songService.findSongByUuid(songUuid);

        if (playlist.getSongs().stream().noneMatch(song -> song.getUuid().equals(songToAdd.getUuid()))) {
            playlistSongsRepository.saveAndFlush(new PlaylistSongs(playlist, songToAdd));
            playlist.getSongs().add(songToAdd);
        }
    }

    private User getAuthUser() {
        return authService.getAuthenticatedUser();
    }

    public Playlist findByUuid(String playlistUuid) {
        return playlistRepository.findByUuid(playlistUuid).orElseThrow(NoSuchElementException::new);
    }

    public PublicSimplePlaylistDto publicSimplePlaylistDto(Playlist playlist) {
        PublicSimplePlaylistDto playlistDto = new PublicSimplePlaylistDto();
        playlistDto.setSongsCount(playlist.getSongs().size());
        playlistDto.setStatus(playlist.getStatus());
        playlistDto.setUuid(playlist.getUuid());
        playlistDto.setName(playlist.getName());
        playlistDto.setSongs(playlist.getSongs().stream().map(songMapper::toSongDto).collect(Collectors.toSet()));

        return playlistDto;
    }

}
