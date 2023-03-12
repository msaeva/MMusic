package bg.softuni.mmusic.model.mapper;

import bg.softuni.mmusic.model.dtos.AddSongDto;
import bg.softuni.mmusic.model.dtos.ProfileDetailedSongDto;
import bg.softuni.mmusic.model.dtos.PublicSimpleSongDto;
import bg.softuni.mmusic.model.dtos.SongDto;
import bg.softuni.mmusic.model.entities.Song;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {
    Song addSongDtoToSong(AddSongDto addSongDto);

    SongDto songEntityToSongDto(Song song);

    PublicSimpleSongDto toPublicSimpleSongDto(Song song);

    ProfileDetailedSongDto toProfileDetailedSongDto(Song song);
}
