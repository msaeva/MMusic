package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.CreatePlaylistDto;
import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.repositories.PlaylistRepository;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {
    private  final AuthService authService;
    private final PlaylistRepository playlistRepository;

    public PlaylistService(AuthService authService, PlaylistRepository playlistRepository) {
        this.authService = authService;
        this.playlistRepository = playlistRepository;
    }

    public void create(CreatePlaylistDto createPlaylistDto) {
        User authUser = authService.getAuthenticatedUser();
        Playlist newPlaylist = new Playlist();
        newPlaylist.setName(createPlaylistDto.getName());
        newPlaylist.setOwner(authUser);
        newPlaylist.setStatus(createPlaylistDto.getStatus());

        playlistRepository.saveAndFlush(newPlaylist);
    }
}
