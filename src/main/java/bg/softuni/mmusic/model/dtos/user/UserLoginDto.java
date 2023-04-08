package bg.softuni.mmusic.model.dtos.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
