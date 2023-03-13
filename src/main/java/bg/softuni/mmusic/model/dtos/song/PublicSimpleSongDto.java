package bg.softuni.mmusic.model.dtos.song;

import bg.softuni.mmusic.model.entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicSimpleSongDto {

    private String title;

    private String description;

    private Long duration;

    private String imageUrl;

    private User author;

    private bg.softuni.mmusic.model.entities.Style Style;

    private Integer favouriteCount;
}
