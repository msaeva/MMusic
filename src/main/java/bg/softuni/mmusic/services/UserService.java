package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.ProfileDetailedPlaylistDto;
import bg.softuni.mmusic.model.dtos.ProfileDetailedSongDto;
import bg.softuni.mmusic.model.dtos.PublicSimplePlaylistDto;
import bg.softuni.mmusic.model.dtos.PublicSimpleSongDto;
import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.enums.PlaylistStatus;
import bg.softuni.mmusic.model.enums.SongStatus;
import bg.softuni.mmusic.model.error.UserNotFoundException;
import bg.softuni.mmusic.model.mapper.PlaylistMapper;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.repositories.PlaylistRepository;
import bg.softuni.mmusic.repositories.SongRepository;
import bg.softuni.mmusic.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    private final SongMapper songMapper;
    private final PlaylistMapper playlistMapper;

    public UserService(UserRepository userRepository, SongRepository songRepository, PlaylistRepository playlistRepository, SongMapper songMapper, PlaylistMapper playlistMapper) {
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
        this.songMapper = songMapper;
        this.playlistMapper = playlistMapper;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<ProfileDetailedSongDto> getUserSongs(User authUser) {
        Optional<List<Song>> userSongs =
                songRepository.findAllByAuthorUuid(authUser.getUuid());

        if (userSongs.isEmpty()) {
            // TODO make exception for user with no songs yet
            return null;
        }
        return userSongs.get().stream().map(songMapper::toProfileDetailedSongDto).toList();

    }

    public List<ProfileDetailedPlaylistDto> getUserPlaylist(User authUser) {
        Optional<List<Playlist>> playlists = playlistRepository.findAllByOwnerUuid(authUser.getUuid());
        if (playlists.isEmpty()) {
            // TODO make exception for user with no playlists yet
            return null;
        }

        return playlists.get().stream().map(playlistMapper::toProfileDetailedPlaylistDto).toList();
    }

    public User getUserByUuid(String uuid) {
        return userRepository.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
    }

    public List<PublicSimpleSongDto> getUserPublicSongs(User user) {
        Optional<List<Song>> songs = songRepository.findAllByAuthorUuidAndStatus(user.getUuid(), SongStatus.PUBLIC);
        if (songs.isEmpty()) {
            //TODO return error
            return null;
        }
        return songs.get().stream().map(songMapper::toPublicSimpleSongDto).toList();
    }


    public List<PublicSimplePlaylistDto> getUserPlaylists(User user) {
        Optional<List<Playlist>> playlists =
                playlistRepository.findAllByOwnerUuidAndStatus(user.getUuid(), PlaylistStatus.PUBLIC);

        if (playlists.isEmpty()) {
            //TODO return error
            return null;
        }

        return playlists.get().stream().map(playlistMapper::toPublicSimplePlaylistDto).toList();
    }
}

