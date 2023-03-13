package bg.softuni.mmusic.model.dtos.song;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublicSimplePlaylistDto {
    private String name;

    private Set<PublicSimpleSongDto> songs;
}
