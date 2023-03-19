package bg.softuni.mmusic.model.dtos.song;

import bg.softuni.mmusic.model.enums.SongStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDetailedSongDto {

    private String uuid;

    private String title;

    private String description;

    private Long duration;

    private String pictureUrl;

    private LocalDate createdDate;

    private bg.softuni.mmusic.model.entities.Style Style;

    private SongStatus status;

    private Integer downloadCount;

    private Integer favouriteCount;
}
