package bg.softuni.mmusic.model.dtos.song;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicSimpleSongDto {

    private String uuid;

    private String title;

    private String description;

    private String pictureUrl;

}
