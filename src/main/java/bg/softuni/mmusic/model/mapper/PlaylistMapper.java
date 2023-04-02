package bg.softuni.mmusic.model.mapper;

import bg.softuni.mmusic.model.dtos.playlist.PublicDetailedPlaylistDto;
import bg.softuni.mmusic.model.dtos.playlist.PublicSimplePlaylistDto;
import bg.softuni.mmusic.model.entities.Playlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = Collectors.class)
public abstract class PlaylistMapper {

    @Autowired
    protected SongMapper songMapper;

    public abstract PublicDetailedPlaylistDto toProfileDetailedPlaylistDto(Playlist playlist);

    @Mapping(target = "songsCount", expression = "java(playlist.getSongs().size())")
    @Mapping(target = "songs", expression =
            "java(playlist.getSongs().stream().map(songMapper::toSongDto).collect(Collectors.toSet()))")
    public abstract PublicSimplePlaylistDto toPublicSimplePlaylistDto(Playlist playlist);

}
