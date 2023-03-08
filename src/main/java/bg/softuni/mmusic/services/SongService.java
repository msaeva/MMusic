package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.AddSongDto;
import bg.softuni.mmusic.model.dtos.SongDto;
import bg.softuni.mmusic.model.dtos.UpdateSongDto;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.Style;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.repositories.SongRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SongService {
    private final SongMapper songMapper;
    private final AuthService authService;
    private final SongRepository songRepository;
    private final StyleService styleService;

    public SongService(SongMapper songMapper, AuthService authService, SongRepository songRepository, StyleService styleService) {
        this.songMapper = songMapper;
        this.authService = authService;
        this.songRepository = songRepository;
        this.styleService = styleService;
    }

    public void addSong(AddSongDto addSongDto) {
        User authUser = authService.getAuthenticatedUser();

        Optional<Style> byStyle = styleService.findByStyle(addSongDto.getStyle());
        if (byStyle.isEmpty()) {
            throw new NoSuchElementException();
        }

        Song songToSave = songMapper.addSongDtoToSong(addSongDto);
        songToSave.setAuthor(authUser);
        songToSave.setStyle(byStyle.get());
        songRepository.saveAndFlush(songToSave);

    }

    public Optional<Song> findSongByUuid(String songUuid) {
        return songRepository.findByUuid(songUuid);
    }

    public SongDto toSongDto(Song song) {
        return songMapper.songEntityToSongDto(song);
    }

    public void update(Song songToUpdate, UpdateSongDto songDto) {
        songToUpdate.setTitle(songDto.getTitle());
        songToUpdate.setDescription(songDto.getDescription());

        songRepository.saveAndFlush(songToUpdate);
    }
}

