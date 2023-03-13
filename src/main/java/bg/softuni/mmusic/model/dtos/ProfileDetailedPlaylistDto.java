package bg.softuni.mmusic.model.dtos;

import bg.softuni.mmusic.model.dtos.song.ProfileDetailedSongDto;
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
public class ProfileDetailedPlaylistDto {

    private String name;

    private Set<ProfileDetailedSongDto> songs;

    private PlaylistStatus status;
}
