package bg.softuni.mmusic.controllers.validations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicSongValidation extends Pageable {
    private String style;
}
