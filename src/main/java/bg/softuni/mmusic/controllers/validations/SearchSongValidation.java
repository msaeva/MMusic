package bg.softuni.mmusic.controllers.validations;

import bg.softuni.mmusic.model.enums.StyleType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchSongValidation extends Pageable {

    private List<String> sort;

    private StyleType style;
}
