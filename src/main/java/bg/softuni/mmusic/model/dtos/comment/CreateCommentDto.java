package bg.softuni.mmusic.model.dtos.comment;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentDto {

    @Size(min = 2, max = 300)
    private String text;
}
