package bg.softuni.mmusic.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
    @Size(min = 4, max = 12)
    @NotNull
    private String username;

    @Size(min = 5, max = 20)
    private String password;

}
