package bg.softuni.mmusic.services;

import bg.softuni.mmusic.controllers.validations.SearchSongValidation;
import bg.softuni.mmusic.model.dtos.AddSongDto;
import bg.softuni.mmusic.model.dtos.SongDto;
import bg.softuni.mmusic.model.dtos.UpdateSongDto;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.Style;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.enums.SongStatus;
import bg.softuni.mmusic.model.error.InvalidSongException;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.repositories.SongRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public void delete(String uuid) {
        User authUser = authService.getAuthenticatedUser();
        if (authUser == null){
            throw new NoSuchElementException("User should be authenticated to delete song!");
        }

        Optional<Song> song = songRepository.findByUuid(uuid);
        if (song.isEmpty()){
            throw new NoSuchElementException("Song with that id does not exist!");
        }

        if (authUser.getOwnSongs().stream()
                .noneMatch(s -> s.getUuid().equals(song.get().getUuid()))){
            throw new NoSuchElementException("User should own the song!");
        }

        this.songRepository.delete(song.get());
    }

    public Page<Song> getAll(SearchSongValidation validation) {
        Pageable pageable = PageRequest.of(validation.getOffset(), validation.getCount());

        return songRepository.findAllByStatus(SongStatus.PUBLIC, pageable);

    }

    public void like(String songUuid) {
        User authUser = authService.getAuthenticatedUser();
        Song songToLike = songRepository.findByUuid(songUuid).orElseThrow(RuntimeException::new);

        if (authUser.getOwnSongs().stream().anyMatch(song -> song.getUuid().equals(songToLike.getUuid()))) {
            throw new InvalidSongException(songUuid, "User cannot like own song");
        }

        songToLike.setLikes(songToLike.getLikes() + 1);
        songRepository.saveAndFlush(songToLike);
    }

    public void unlike(String songUuid) {
        User authUser = authService.getAuthenticatedUser();
        Song songToLike = songRepository.findByUuid(songUuid).orElseThrow(RuntimeException::new);

        if (authUser.getOwnSongs().stream().anyMatch(song -> song.getUuid().equals(songToLike.getUuid()))) {
            throw new InvalidSongException(songUuid, "User cannot like own song");
        }

        songToLike.setLikes(songToLike.getLikes() + 1);
        songRepository.saveAndFlush(songToLike);
    }
}

