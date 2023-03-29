package bg.softuni.mmusic.services;

import bg.softuni.mmusic.controllers.validations.PublicSongValidation;
import bg.softuni.mmusic.controllers.validations.SearchSongValidation;
import bg.softuni.mmusic.model.dtos.song.AddSongDto;
import bg.softuni.mmusic.model.dtos.song.PublicDetailedSongDto;
import bg.softuni.mmusic.model.dtos.song.PublicSimpleSongDto;
import bg.softuni.mmusic.model.dtos.song.UpdateSongDto;
import bg.softuni.mmusic.model.entities.*;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.model.enums.SongStatus;
import bg.softuni.mmusic.model.error.InvalidSongException;
import bg.softuni.mmusic.model.mapper.SongMapper;
import bg.softuni.mmusic.repositories.PlaylistSongsRepository;
import bg.softuni.mmusic.repositories.SongRepository;
import bg.softuni.mmusic.repositories.UserFavouriteSongsRepository;
import bg.softuni.mmusic.repositories.UserLikedSongsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SongService {
    private final SongMapper songMapper;
    private final AuthService authService;
    private final SongRepository songRepository;
    private final StyleService styleService;
    private final ImageCloudService imageCloudService;
    private final PlaylistSongsRepository playlistSongsRepository;
    private final UserFavouriteSongsRepository favouriteSongsRepository;
    private final UserLikedSongsRepository likedSongsRepository;

    public SongService(SongMapper songMapper,
                       AuthService authService,
                       SongRepository songRepository,
                       StyleService styleService,
                       ImageCloudService imageCloudService,
                       PlaylistSongsRepository playlistSongsRepository,
                       UserFavouriteSongsRepository favouriteSongsRepository,
                       UserLikedSongsRepository userLikedSongsRepository) {

        this.songMapper = songMapper;
        this.authService = authService;
        this.songRepository = songRepository;
        this.styleService = styleService;
        this.imageCloudService = imageCloudService;
        this.playlistSongsRepository = playlistSongsRepository;
        this.favouriteSongsRepository = favouriteSongsRepository;
        this.likedSongsRepository = userLikedSongsRepository;
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
        picture.setTitle(addSongDto.getImage().getOriginalFilename());
        songToSave.setPicture(picture);

        songRepository.saveAndFlush(songToSave);
    }

    public Song findSongByUuid(String songUuid) {
        return songRepository.findByUuid(songUuid).orElseThrow(NoSuchElementException::new);
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

        List<PlaylistSongs> playlists = playlistSongsRepository.findBySongUuid(song.getUuid());
        playlists.removeIf(playlist -> playlist.getSong().getUuid().equals(song.getUuid()));

        this.playlistSongsRepository.saveAllAndFlush(playlists);
        this.songRepository.delete(song);
    }

    public Page<Song> getAllPublic(PublicSongValidation validation) {
        Pageable pageable = PageRequest.of(validation.getOffset(), validation.getCount());

        return songRepository.findAllByStatus(SongStatus.PUBLIC, pageable);

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

    public Set<PublicSimpleSongDto> toPublicSimpleSongDto(Set<Song> songs) {
        return songs.stream().map(songMapper::toPublicSimpleSongDto).collect(Collectors.toSet());
    }

    public PublicDetailedSongDto toDetailedSongDto(Song song) {
        return songMapper.toPublicDetailedSongDto(song);
    }

    public List<PublicSimpleSongDto> findAllPublicToAddToPlaylist(String playlistUuid) {

        List<Song> songsToAdd = songRepository.findAllNotInPlaylist(playlistUuid, SongStatus.PUBLIC);

        return songsToAdd.stream().map(songMapper::toPublicSimpleSongDto).
                collect(Collectors.toList());
    }

    public void like(String songUuid) {
        User authUser = authService.getAuthenticatedUser();
        Song songToLike = songRepository.findByUuid(songUuid).orElseThrow(NoSuchElementException::new);

        if (authUser.getOwnSongs().stream().anyMatch(song -> song.getUuid().equals(songToLike.getUuid()))) {
            throw new InvalidSongException("User cannot like own song");
        }

        List<String> userLikedSongsUuids = likedSongsRepository.getUserLikedSongs(authUser.getUuid());
        if (userLikedSongsUuids.stream().anyMatch(uuid -> uuid.equals(songToLike.getUuid()))) {
            throw new InvalidSongException("You have already like this song!");
        }

        songToLike.setLikes(songToLike.getLikes() + 1);
        songRepository.saveAndFlush(songToLike);
        likedSongsRepository.save(new UserLikedSongs(authUser.getUuid(), songUuid));

    }

    public void unlike(String songUuid) {
        User authUser = authService.getAuthenticatedUser();
        Song songToUnlike = songRepository.findByUuid(songUuid).orElseThrow(RuntimeException::new);

        if (authUser.getOwnSongs().stream().anyMatch(song -> song.getUuid().equals(songToUnlike.getUuid()))) {
            throw new InvalidSongException("User cannot like own song");
        }
        List<String> userLikedSongsUuids = likedSongsRepository.getUserLikedSongs(authUser.getUuid());
        if (userLikedSongsUuids.stream().noneMatch(uuid -> uuid.equals(songToUnlike.getUuid()))) {
            throw new InvalidSongException("You did not like this song!");
        }

        songToUnlike.setLikes(songToUnlike.getLikes() - 1);
        songRepository.saveAndFlush(songToUnlike);
        UserLikedSongs toDelete = likedSongsRepository.getBySongAndUser(authUser.getUuid(), songUuid);
        likedSongsRepository.delete(toDelete);
    }

    public void addToFavourite(String songUuid, User user) {
        Song songToAddToFav = songRepository.findByUuid(songUuid).orElseThrow(RuntimeException::new);

        List<String> userFavouriteSongUuids = favouriteSongsRepository.getUserFavouriteSongs(user.getUuid());
        if (userFavouriteSongUuids.stream().anyMatch(uuid -> uuid.equals(songToAddToFav.getUuid()))) {
            throw new InvalidSongException("You cannot add own song to favourite!");
        }

        if (userFavouriteSongUuids.stream().anyMatch(uuid -> uuid.equals(songToAddToFav.getUuid()))) {
            throw new InvalidSongException("You have already added this song to favorites!");
        }
        songToAddToFav.setFavouriteCount(songToAddToFav.getFavouriteCount() + 1);
        songRepository.saveAndFlush(songToAddToFav);
        favouriteSongsRepository.save(new UserFavouriteSongs(user.getUuid(), songUuid));
    }

    public void removeFromFavourite(String songUuid, User user) {
        Song songToRemove = songRepository.findByUuid(songUuid).orElseThrow(RuntimeException::new);

        List<String> userFavouriteSongUuids = favouriteSongsRepository.getUserFavouriteSongs(user.getUuid());
        if (userFavouriteSongUuids.stream().noneMatch(uuid -> uuid.equals(songToRemove.getUuid()))) {
            throw new InvalidSongException("Song is not added to favourites");
        }
        songToRemove.setFavouriteCount(songToRemove.getFavouriteCount() - 1);
        songRepository.saveAndFlush(songToRemove);

        UserFavouriteSongs toDelete =
                favouriteSongsRepository.getBySongAndUser(user.getUuid(), songUuid);
        favouriteSongsRepository.delete(toDelete);
    }
}

