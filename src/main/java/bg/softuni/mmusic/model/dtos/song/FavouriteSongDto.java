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
    private String title;

    private String description;

    private Long duration;

    private String imageUrl;

    private bg.softuni.mmusic.model.entities.Style Style;

}
