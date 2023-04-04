package bg.softuni.mmusic.init;

import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.repositories.UserRepository;
import bg.softuni.mmusic.repositories.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class UserSeeder {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository roleRepository;

    public UserSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder, UserRoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void init() {
        if (userRepository.count() == 0) {
            initUsers();
        }
    }

    public void initUsers() {
        UserRole adminRole = roleRepository.findByRole(Role.ADMIN).orElseThrow(NoSuchElementException::new);
        UserRole userRole = roleRepository.findByRole(Role.USER).orElseThrow(NoSuchElementException::new);
        UserRole musicianRole = roleRepository.findByRole(Role.MUSICIAN).orElseThrow(NoSuchElementException::new);

        User admin = User.builder()
                .username("admin")
                .firstName("admin")
                .lastName("admin")
                .email("admin@abv.bg")
                .password(passwordEncoder.encode("admin"))
                .createdDate(LocalDate.now())
                .roles(Set.of(adminRole))
                .build();

        User user = User.builder()
                .username("mariyasaeva")
                .firstName("mariya")
                .lastName("saeva")
                .email("mariya@abv.bg")
                .password(passwordEncoder.encode("mariya"))
                .createdDate(LocalDate.now())
                .roles(Set.of(userRole))
                .build();

        User musician = User.builder()
                .username("ivana")
                .firstName("ivana")
                .lastName("petrova")
                .email("ivana@abv.bg")
                .password(passwordEncoder.encode("ivana"))
                .createdDate(LocalDate.now())
                .roles(Set.of(musicianRole))
                .build();

        userRepository.saveAllAndFlush(List.of(admin, user, musician));
    }
}
