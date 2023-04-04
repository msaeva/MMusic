package bg.softuni.mmusic.model.dtos.song;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongDto {
    private String uuid;

    private String title;

    private String description;

    private String pictureUrl;

    private String style;

    private String authorUuid;

    private String authorUsername;

    private int likes;
}
