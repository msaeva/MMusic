package bg.softuni.mmusic.model.dtos.playlist;

import bg.softuni.mmusic.model.enums.PlaylistStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublicSimplePlaylistDto {
    private String name;

    private Integer songsCount;

    private PlaylistStatus status;
}
