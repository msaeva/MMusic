package bg.softuni.mmusic.model.dtos.user;

import bg.softuni.mmusic.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    @Size(min = 4, max = 12, message = "Username length must be between 4 and 12 symbols")
    @NotNull
    private String username;

    @Size(min = 2, max = 15)
    @NotNull
    private String firstName;

    @Size(min = 2, max = 15)
    @NotNull
    private String lastName;

    @NotEmpty(message = "Email cannot be empty!")
    @Email
    private String email;

    @NotNull
    @Size(min = 5, max = 20)
    private String password;

    @NotNull()
    @Size(min = 5, max = 20, message = "Password length must be minimum 5 symbols!")
    private String confirmPassword;

    @NotNull
    @Size(min = 1, message = "Choose at least 1 role")
    private Set<Role> roles;
}
