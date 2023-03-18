package bg.softuni.mmusic.controllers.validations;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchSongValidation extends Pageable {

    private List<String> sort;

    private String style;
}
