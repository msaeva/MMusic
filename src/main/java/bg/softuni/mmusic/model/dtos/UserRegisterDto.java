package bg.softuni.mmusic.model.dtos;

import bg.softuni.mmusic.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    @Size(min = 4, max = 12)
    @NotNull
    private String username;

    @Size(min = 2, max = 15)
    @NotNull
    private String firstName;

    @Size(min = 2, max = 15)
    @NotNull
    private String lastName;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 5, max = 20)
    private String password;

    @NotNull
    @Size(min = 5, max = 20)
    private String confirmPassword;

    @NotNull
    private Set<Role> roles;
}
