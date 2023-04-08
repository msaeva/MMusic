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
                .firstName("Admin")
                .lastName("Adminov")
                .email("admin@abv.bg")
                .password(passwordEncoder.encode("admin"))
                .createdDate(LocalDate.now())
                .roles(Set.of(adminRole))
                .isActivated(true)
                .build();

        User user = User.builder()
                .username("mariyasaeva")
                .firstName("Mariya")
                .lastName("Saeva")
                .email("mariya@abv.bg")
                .password(passwordEncoder.encode("mariya"))
                .createdDate(LocalDate.now())
                .roles(Set.of(userRole))
                .isActivated(true)
                .build();

        User user2 = User.builder()
                .username("ivana")
                .firstName("Ivana")
                .lastName("Petrova")
                .email("ivana@abv.bg")
                .password(passwordEncoder.encode("ivana"))
                .createdDate(LocalDate.now())
                .roles(Set.of(musicianRole))
                .isActivated(true)
                .build();

        User pink = User.builder()
                .username("pink_")
                .firstName("Alecia")
                .lastName("Moore")
                .email("pink@abv.bg")
                .password(passwordEncoder.encode("pink123"))
                .createdDate(LocalDate.now())
                .roles(Set.of(musicianRole))
                .isActivated(true)
                .build();


        User madonna = User.builder()
                .username("madonna")
                .firstName("Madonna")
                .lastName("Ciccone")
                .email("ciccone@abv.bg")
                .password(passwordEncoder.encode("madonna"))
                .createdDate(LocalDate.now())
                .roles(Set.of(musicianRole))
                .isActivated(true)
                .build();

        User turner = User.builder()
                .username("tinaturner")
                .firstName("Tina")
                .lastName("Turner")
                .email("turner@gmail.com")
                .password(passwordEncoder.encode("turner"))
                .createdDate(LocalDate.now())
                .roles(Set.of(musicianRole))
                .isActivated(true)
                .build();

        User beyonce = User.builder()
                .username("beyonce")
                .firstName("Beyonce")
                .lastName("Giselle")
                .email("beyonce@gmail.com")
                .password(passwordEncoder.encode("beyonce"))
                .createdDate(LocalDate.now())
                .roles(Set.of(musicianRole))
                .isActivated(true)
                .build();

        User rihanna = User.builder()
                .username("rihanna")
                .firstName("Rihanna")
                .lastName("Fenty")
                .email("rihanna@gmail.com")
                .password(passwordEncoder.encode("rihanna"))
                .createdDate(LocalDate.now())
                .roles(Set.of(musicianRole))
                .isActivated(true)
                .build();

        User eminem = User.builder()
                .username("eminem")
                .firstName("Marshall")
                .lastName("Bruce")
                .email("eminem@gmail.com")
                .password(passwordEncoder.encode("eminem"))
                .createdDate(LocalDate.now())
                .roles(Set.of(musicianRole))
                .isActivated(true)
                .build();

        userRepository.saveAllAndFlush(List.of(admin, user, user2, pink, madonna, turner, beyonce, rihanna, eminem));
    }
}
