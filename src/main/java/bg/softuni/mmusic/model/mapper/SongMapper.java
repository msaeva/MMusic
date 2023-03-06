package bg.softuni.mmusic.model.mapper;

import bg.softuni.mmusic.model.dtos.AddSongDto;
import bg.softuni.mmusic.model.entities.Song;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {
    Song addSongDtoToSong(AddSongDto addSongDto);
}
