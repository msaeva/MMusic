package bg.softuni.mmusic.model.dtos.playlist;

import bg.softuni.mmusic.model.dtos.song.SongDto;
import bg.softuni.mmusic.model.enums.PlaylistStatus;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicSimplePlaylistDto {
    private String uuid;

    private String name;

    private Integer songsCount;

    private PlaylistStatus status;

    private Set<SongDto> songs;
}
