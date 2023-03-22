package bg.softuni.mmusic.model.mapper;

import bg.softuni.mmusic.model.dtos.playlist.PublicDetailedPlaylistDto;
import bg.softuni.mmusic.model.dtos.playlist.PublicSimplePlaylistDto;
import bg.softuni.mmusic.model.entities.Playlist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {

    PublicDetailedPlaylistDto toProfileDetailedPlaylistDto(Playlist playlist);

    PublicSimplePlaylistDto toPublicSimplePlaylistDto(Playlist playlist);
}
