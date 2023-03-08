package bg.softuni.mmusic.model.dtos;

import bg.softuni.mmusic.model.entities.Style;
import bg.softuni.mmusic.model.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {
    private String title;

    private String description;

    private Long duration;

    private String imageUrl;

    private User author;

    private LocalDate createdDate = LocalDate.now();

    private Style Style;

    private Integer downloadCount;

    private Integer favouriteCount;
}
