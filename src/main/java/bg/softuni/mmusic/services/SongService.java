package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.AddSongDto;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.repositories.SongRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SongService {
    private final SongMapper songMapper;
    private final AuthService authService;
    private final SongRepository songRepository;

    public SongService(SongMapper songMapper, AuthService authService, SongRepository songRepository) {
        this.songMapper = songMapper;
        this.authService = authService;
        this.songRepository = songRepository;
    }

    public void addSong(AddSongDto addSongDto) {
        User authUser = authService.getAuthenticatedUser();

        Song songToSave = songMapper.addSongDtoToSong(addSongDto);
        songToSave.setAuthor(authUser);

        songRepository.saveAndFlush(songToSave);

    }

    public Song findSongByUuid(String songUuid) {
        return songRepository.findById(songUuid).get();
    }
}

