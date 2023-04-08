package bg.softuni.mmusic.init;

import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.enums.PlaylistStatus;
import bg.softuni.mmusic.model.error.InvalidUserException;
import bg.softuni.mmusic.repositories.PlaylistRepository;
import bg.softuni.mmusic.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistSeeder {
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;

    public PlaylistSeeder(PlaylistRepository playlistRepository,
                          UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
    }

    public void init() {

        if (playlistRepository.count() == 0) {

            User eminem = userRepository.findByUsername("eminem")
                    .orElseThrow(() -> new InvalidUserException("User with username eminem not found!"));

            User rihanna = userRepository.findByUsername("rihanna")
                    .orElseThrow(() -> new InvalidUserException("User with username rihanna not found!"));

            User ivana = userRepository.findByUsername("ivana")
                    .orElseThrow(() -> new InvalidUserException("User with username madonna not found!"));

            Playlist playlist = Playlist.builder()
                    .name("Mega Hit Mix")
                    .owner(ivana)
                    .status(PlaylistStatus.PUBLIC)
                    .build();

            Playlist playlist2 = Playlist.builder()
                    .name("My POP Playlist")
                    .owner(ivana)
                    .status(PlaylistStatus.PRIVATE)
                    .build();

            Playlist playlist3 = Playlist.builder()
                    .name("2022 Top Hits")
                    .owner(eminem)
                    .status(PlaylistStatus.PUBLIC)
                    .build();

            Playlist playlist4 = Playlist.builder()
                    .name("2021 Top Hits")
                    .owner(rihanna)
                    .status(PlaylistStatus.PUBLIC)
                    .build();


            Playlist playlist5 = Playlist.builder()
                    .name("Rihanna - 2023")
                    .owner(rihanna)
                    .status(PlaylistStatus.PUBLIC)
                    .build();

            playlistRepository.saveAllAndFlush(List.of(playlist, playlist2, playlist3, playlist4, playlist5));
        }
    }
}
