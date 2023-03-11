package bg.softuni.mmusic.controllers.validations;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchSongValidation extends Pageable {

    @NotNull
    private String song;
}
