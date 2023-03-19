package bg.softuni.mmusic.services;

import bg.softuni.mmusic.controllers.validations.PublicSongValidation;
import bg.softuni.mmusic.controllers.validations.SearchSongValidation;
import bg.softuni.mmusic.model.dtos.song.AddSongDto;
import bg.softuni.mmusic.model.dtos.song.PublicSimpleSongDto;
import bg.softuni.mmusic.model.dtos.song.SongDto;
import bg.softuni.mmusic.model.dtos.song.UpdateSongDto;
import bg.softuni.mmusic.model.entities.Picture;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.Style;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.model.enums.SongStatus;
import bg.softuni.mmusic.model.error.InvalidSongException;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.repositories.SongRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class SongService {
    private final SongMapper songMapper;
    private final AuthService authService;
    private final SongRepository songRepository;
    private final StyleService styleService;
    private final ImageCloudService imageCloudService;

    public SongService(SongMapper songMapper,
                       AuthService authService,
                       SongRepository songRepository,
                       StyleService styleService,
                       ImageCloudService imageCloudService) {

        this.songMapper = songMapper;
        this.authService = authService;
        this.songRepository = songRepository;
        this.styleService = styleService;
        this.imageCloudService = imageCloudService;
    }

    public void addSong(AddSongDto addSongDto) {
        User authUser = authService.getAuthenticatedUser();

        if (authUser == null) {
            throw new NoSuchElementException("User should be authenticated to update song!");
        }
        if (authUser.getRoles().stream().noneMatch(user -> user.getRole().equals(Role.MUSICIAN))) {
            throw new NoSuchElementException("You do not have permissions to add a song");
        }

        Optional<Style> byStyle = styleService.findByStyle(addSongDto.getStyle());
        if (byStyle.isEmpty()) {
            throw new NoSuchElementException();
        }

        Song songToSave = songMapper.addSongDtoToSong(addSongDto);
        songToSave.setAuthor(authUser);
        songToSave.setStyle(byStyle.get());

        String pictureUrl = imageCloudService.saveImage(addSongDto.getImage());

        Picture picture = new Picture();
        picture.setSong(songToSave);
        picture.setUrl(pictureUrl);
        songToSave.setPicture(picture);

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
        songToUpdate.setStyle(songDto.getStyle());
        songToUpdate.setStatus(songDto.getStatus());

        songRepository.saveAndFlush(songToUpdate);
    }

    public void delete(String uuid) {
        User authUser = authService.getAuthenticatedUser();
        if (authUser == null) {
            throw new NoSuchElementException("User should be authenticated to delete song!");
        }

        Song song = songRepository.findByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Song with that id does not exist!"));

        if (authUser.getOwnSongs().stream()
                .noneMatch(s -> s.getUuid().equals(song.getUuid()))) {
            throw new NoSuchElementException("User should own the song!");
        }
        this.songRepository.delete(song);
    }

    public Page<Song> getAllPublic(PublicSongValidation validation) {
        Pageable pageable = PageRequest.of(validation.getOffset(), validation.getCount());

        return songRepository.findAllByStatus(SongStatus.PUBLIC, pageable);

    }

    public void like(String songUuid) {
        User authUser = authService.getAuthenticatedUser();
        Song songToLike = songRepository.findByUuid(songUuid).orElseThrow(RuntimeException::new);

        if (authUser.getOwnSongs().stream().anyMatch(song -> song.getUuid().equals(songToLike.getUuid()))) {
            throw new InvalidSongException(songUuid, "User cannot like own song");
        }
        if (authUser.getLikedSongs().stream().anyMatch(song -> song.getUuid().equals(songToLike.getUuid()))) {
            throw new InvalidSongException(songUuid, "You have already like this song!");
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
        if (authUser.getLikedSongs().stream().noneMatch(song -> song.getUuid().equals(songToLike.getUuid()))) {
            throw new InvalidSongException(songUuid, "You did not like this song!");
        }

        songToLike.setLikes(songToLike.getLikes() + 1);
        songRepository.saveAndFlush(songToLike);
    }

    public UpdateSongDto toUpdateSongDto(Song songToUpdate) {
        return songMapper.toUpdateSongDto(songToUpdate);
    }

    public Page<Song> getAll(SearchSongValidation validation) {
        List<String> sorts = validation.getSort() != null ? validation.getSort() : List.of();

        List<Sort.Order> orders = sorts.stream()
                .map(sort -> sort.split(":"))
                .map(sort -> {
                    Sort.Direction direction = Integer.parseInt(sort[1]) >= 0 ? Sort.Direction.DESC : Sort.Direction.ASC;
                    return new Sort.Order(direction, sort[0]);
                }).toList();

        Pageable pageable = PageRequest.of(validation.getOffset(), validation.getCount(), Sort.by(orders));
        return songRepository.findAll(pageable);

    }

    public List<PublicSimpleSongDto> toPublicSimpleSongDto(Page<Song> songs) {
        List<PublicSimpleSongDto> publicSimpleSongDtos = new ArrayList<>();
        for (Song song : songs) {
            PublicSimpleSongDto dto = songMapper.toPublicSimpleSongDto(song);
            dto.setPictureUrl(song.getPicture().getUrl());
            publicSimpleSongDtos.add(dto);
        }
        return publicSimpleSongDtos;
    }
}

