package bg.softuni.mmusic.model.dtos;

import bg.softuni.mmusic.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {

//    @NotNull
    private String uuid;

    @Size(min = 4, max = 12)
    @NotNull
    private String username;

    @Size(min = 2, max = 15)
    @NotNull
    private String fullName;

    @Email
    @NotNull
    private String email;

    private Role role;
}
