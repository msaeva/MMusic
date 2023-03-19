package bg.softuni.mmusic.model.dtos.playlist;

import bg.softuni.mmusic.model.dtos.song.PublicSimpleSongDto;
import bg.softuni.mmusic.model.enums.PlaylistStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicDetailedPlaylistDto {

    private String name;

    private Set<PublicSimpleSongDto> songs;

    private PlaylistStatus status;
}
