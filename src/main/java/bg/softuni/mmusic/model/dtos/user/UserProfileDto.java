package bg.softuni.mmusic.model.dtos.user;

import bg.softuni.mmusic.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {

    @NotNull
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

    @Size(max = 300)
    private String about;

    private Set<Role> roles;
}
