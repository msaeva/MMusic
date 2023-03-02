package bg.softuni.mmusic.model.dtos;

import bg.softuni.mmusic.model.enums.StyleType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddSongDto {

    @Size(min = 5, max = 12)
    @NotNull
    private String title;

    @Size(max = 100)
    @NotNull
    private String description;

    @Positive
    @NotNull
    private Long duration;

    private String imageUrl;

    @NotNull
    private StyleType Style;
}
