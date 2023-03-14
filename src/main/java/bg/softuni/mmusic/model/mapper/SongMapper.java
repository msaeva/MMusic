package bg.softuni.mmusic.model.mapper;

import bg.softuni.mmusic.model.dtos.song.*;
import bg.softuni.mmusic.model.entities.Song;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {
    Song addSongDtoToSong(AddSongDto addSongDto);

    SongDto songEntityToSongDto(Song song);

    PublicSimpleSongDto toPublicSimpleSongDto(Song song);

    ProfileDetailedSongDto toProfileDetailedSongDto(Song song);

    FavouriteSongDto toFavouriteSongDto(Song song);

    UpdateSongDto toUpdateSongDto(Song songToUpdate);

}
