package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.user.ModifyRolesDto;
import bg.softuni.mmusic.model.dtos.user.UserProfileDto;
import bg.softuni.mmusic.model.dtos.playlist.PublicSimplePlaylistDto;
import bg.softuni.mmusic.model.dtos.song.FavouriteSongDto;
import bg.softuni.mmusic.model.dtos.song.PublicSimpleSongDto;
import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.model.enums.PlaylistStatus;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.model.enums.SongStatus;
import bg.softuni.mmusic.model.error.UserNotFoundException;
import bg.softuni.mmusic.model.mapper.PlaylistMapper;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.model.mapper.UserMapper;
import bg.softuni.mmusic.repositories.PlaylistRepository;
import bg.softuni.mmusic.repositories.SongRepository;
import bg.softuni.mmusic.repositories.UserRepository;
import bg.softuni.mmusic.repositories.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;
    private final SongMapper songMapper;
    private final PlaylistMapper playlistMapper;
    private final UserMapper userMapper;
    private final UserRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       SongRepository songRepository,
                       PlaylistRepository playlistRepository,
                       SongMapper songMapper,
                       PlaylistMapper playlistMapper,
                       UserMapper userMapper,
                       UserRoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.playlistRepository = playlistRepository;
        this.songMapper = songMapper;
        this.playlistMapper = playlistMapper;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<PublicSimpleSongDto> getUserSongs(User authUser) {
        Optional<List<Song>> userSongs =
                songRepository.findAllByAuthorUuid(authUser.getUuid());

        if (userSongs.isEmpty()) {
            return Collections.emptyList();
        }
        return userSongs.get().stream().map(songMapper::toPublicSimpleSongDto).collect(Collectors.toList());
    }

    public List<PublicSimplePlaylistDto> getUserPlaylist(User authUser) {
        Optional<List<Playlist>> playlists = playlistRepository.findAllByOwnerUuid(authUser.getUuid());
        if (playlists.isEmpty()) {
            return Collections.emptyList();
        }
        return playlists.get().stream().map(playlistMapper::toPublicSimplePlaylistDto).collect(Collectors.toList());
    }

    public User getUserByUuid(String uuid) {
        return userRepository.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid));
    }

    public UserProfileDto getUserProfileInfo(String uuid) {
        User user = getUserByUuid(uuid);
        UserProfileDto userProfileDto = userMapper.toUserProfileDetailsDto(user);

        Set<Role> roles = user.getRoles().stream().map(UserRole::getRole).collect(Collectors.toSet());
        userProfileDto.setRoles(roles);

        return userProfileDto;
    }

    public List<PublicSimpleSongDto> getUserPublicSongs(User user) {
        Optional<List<Song>> songs = songRepository
                .findAllByAuthorUuidAndStatus(user.getUuid(), SongStatus.PUBLIC);

        if (songs.isEmpty()) {
            return Collections.emptyList();
        }
        return songs.get().stream().map(songMapper::toPublicSimpleSongDto).collect(Collectors.toList());
    }


    public List<PublicSimplePlaylistDto> getUserPublicPlaylists(User user) {
        Optional<List<Playlist>> playlists =
                playlistRepository.findAllByOwnerUuidAndStatus(user.getUuid(), PlaylistStatus.PUBLIC);

        if (playlists.isEmpty()) {
            return Collections.emptyList();
        }
        return playlists.get().stream().map(playlistMapper::toPublicSimplePlaylistDto).toList();
    }

    public List<FavouriteSongDto> getFavouriteSongs(User authUser) {

        List<Song> favouriteSongs = userRepository.getUserFavouriteSongs(authUser.getUuid());

        if (favouriteSongs.isEmpty()) {
            return Collections.emptyList();
        }

        return favouriteSongs.stream().map(songMapper::toFavouriteSongDto).collect(Collectors.toList());
    }

    public void update(User userToUpdate, UserProfileDto userProfileDto) {
        String[] fullName = userProfileDto.getFullName().split("\\s+");

        userToUpdate.setFirstName(fullName[0]);
        userToUpdate.setLastName(fullName[1]);

        if (userProfileDto.getAbout().isEmpty()) {
            userToUpdate.setAbout(null);
        } else {
            userToUpdate.setAbout(userProfileDto.getAbout());
        }

        userRepository.saveAndFlush(userToUpdate);
        log.info("Update {}.", userToUpdate);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void modifyRoles(String userUuid, ModifyRolesDto modifyRolesDto) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new UserNotFoundException(userUuid));

        Set<UserRole> roles = new HashSet<>(roleRepository
                .findALlByUuidIn(modifyRolesDto.getRoles()));

        Set<UserRole> rolesToAdd = new HashSet<>();

        for (UserRole role : roles) {
            if (!user.getRoles().contains(role)) {
                rolesToAdd.add(role);
            }
        }

        user.setRoles(rolesToAdd);
        userRepository.saveAndFlush(user);
        log.info("Change role to {}", user);
    }

    @Transactional
    public void changePassword(String oldPassword, String newPassword, User user) {

        if (passwordEncoder.matches(oldPassword, newPassword)) {
            throw new RuntimeException("Old and new passwords are the same");
        }
        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);
        userRepository.saveAndFlush(user);
        log.info("Changed password for User: {}", user);
    }

    public void deleteUnActivatedUsers(LocalDate period) {
        userRepository.deleteAll(userRepository.getUnActivatedUsers(period));
    }
}

