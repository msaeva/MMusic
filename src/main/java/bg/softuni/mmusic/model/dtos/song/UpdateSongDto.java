package bg.softuni.mmusic.model.dtos.song;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSongDto {
    @Size(min = 5, max = 12)
    @NotNull
    private String title;

    @Size(max = 100)
    @NotNull
    private String description;
}
