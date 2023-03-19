package bg.softuni.mmusic.model.dtos.playlist;

import bg.softuni.mmusic.model.enums.PlaylistStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlaylistDto {

    @NotNull
    @Size(min = 4, max = 20)
    private String name;

    @NotNull
    private PlaylistStatus status;
}
