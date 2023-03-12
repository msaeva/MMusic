package bg.softuni.mmusic.controllers.validations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchSongValidation extends Pageable {
    private String song;
}
