package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.playlist.CreatePlaylistDto;
import bg.softuni.mmusic.model.dtos.playlist.PublicSimplePlaylistDto;
import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.entities.PlaylistSongs;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.enums.SongStatus;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.repositories.PlaylistRepository;
import bg.softuni.mmusic.repositories.PlaylistSongsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlaylistService {
    private final AuthService authService;
    private final PlaylistRepository playlistRepository;
    private final SongService songService;
    private final PlaylistSongsRepository playlistSongsRepository;
    private final SongMapper songMapper;

    @Autowired

    public PlaylistService(AuthService authService,
                           PlaylistRepository playlistRepository,
                           SongService songService,
                           PlaylistSongsRepository playlistSongsRepository,
                           SongMapper songMapper) {
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
            log.info("Added {} to {}", songToAdd, playlist);
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

    public HashMap<Playlist, Integer> getTopPlaylists() {
        HashMap<Playlist, Integer> hashMap = new HashMap<>();
        List<Playlist> topPlaylists = playlistRepository.getTopPlaylists(SongStatus.PUBLIC);
        for (Playlist playlist : topPlaylists) {
            int totalLikes = 0;
            for (Song song : playlist.getSongs()) {
                totalLikes += song.getLikes();
            }
            hashMap.put(playlist, totalLikes);
        }
        return hashMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    }

    public boolean checkIsOwner(Playlist playlist, User user) {
        boolean isOwner = false;
        if (user != null) {
            if (playlist.getOwner().getUuid().equals(user.getUuid())) {
                isOwner = true;
            }
        }
        return isOwner;
    }


}
