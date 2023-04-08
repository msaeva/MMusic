package bg.softuni.mmusic.model.dtos.song;

import bg.softuni.mmusic.model.enums.SongStatus;
import bg.softuni.mmusic.model.enums.StyleType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddSongDto {

    @Size(min = 3, max = 300)
    @NotNull
    private String title;

    @Size(max = 500)
    @NotNull
    private String description;

    @NotNull
    private SongStatus status;

    private MultipartFile image;

    @NotNull
    private String videoUrl;

    @NotNull
    private StyleType Style;
}
