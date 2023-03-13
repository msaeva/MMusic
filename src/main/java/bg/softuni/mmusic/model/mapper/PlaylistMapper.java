package bg.softuni.mmusic.model.mapper;

import bg.softuni.mmusic.model.dtos.ProfileDetailedPlaylistDto;
import bg.softuni.mmusic.model.dtos.song.PublicSimplePlaylistDto;
import bg.softuni.mmusic.model.entities.Playlist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {

    ProfileDetailedPlaylistDto toProfileDetailedPlaylistDto(Playlist playlist);

    PublicSimplePlaylistDto toPublicSimplePlaylistDto(Playlist playlist);
}
