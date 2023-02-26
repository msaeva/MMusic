package bg.softuni.mmusic.model.dtos;

import bg.softuni.mmusic.model.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @Size(min = 5, max = 20)
    private String password;

    @Size(min = 5, max = 20)
    private String confirmPassword;

//    @Enumerated(EnumType.STRING)
//    @NotNull
//    private Role role;
}
