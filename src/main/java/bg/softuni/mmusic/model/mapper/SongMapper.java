package bg.softuni.mmusic.model.mapper;

import bg.softuni.mmusic.model.dtos.song.*;
import bg.softuni.mmusic.model.entities.Picture;
import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.repositories.PictureRepository;
import bg.softuni.mmusic.repositories.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", imports = {Picture.class, User.class})
public abstract class SongMapper {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PictureRepository pictureRepository;

    public abstract Song addSongDtoToSong(AddSongDto addSongDto);

    @Mapping(target = "pictureUrl", expression = "java(pictureRepository.getUrlByUuid(song.getPicture().getUuid()))")
    public abstract PublicSimpleSongDto toPublicSimpleSongDto(Song song);

    public abstract PublicDetailedSongDto toPublicDetailedSongDto(Song song);

    @Mapping(target = "pictureUrl", expression = "java(pictureRepository.getUrlByUuid(song.getPicture().getUuid()))")
    public abstract FavouriteSongDto toFavouriteSongDto(Song song);

    public abstract UpdateSongDto toUpdateSongDto(Song songToUpdate);

    @Mapping(target = "authorUuid", expression = "java(song.getAuthor().getUuid())")
    @Mapping(target = "authorUsername", expression = "java(userRepository.getUsernameByUuid(song.getAuthor().getUuid()))")
    @Mapping(target = "pictureUrl", expression = "java(pictureRepository.getUrlByUuid(song.getPicture().getUuid()))")
    @Mapping(target = "style", expression = "java(song.getStyle().getType().name())")
    public abstract SongDto toSongDto(Song song);

}
