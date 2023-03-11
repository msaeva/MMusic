package bg.softuni.mmusic.controllers.validations;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Pageable {
    @NotNull
    private Integer offset = 0;

    @NotNull
    private Integer count = 5;
}
