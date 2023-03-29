package bg.softuni.mmusic.model.dtos.song;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteSongDto {
    private String uuid;
    private String title;

    private String description;

    private String pictureUrl;

    private Integer likes;
    private Integer favouriteCount;


}
