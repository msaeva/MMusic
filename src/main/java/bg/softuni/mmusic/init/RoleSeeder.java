package bg.softuni.mmusic.init;

import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.repositories.UserRepository;
import bg.softuni.mmusic.repositories.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleSeeder implements CommandLineRunner {
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    public RoleSeeder(UserRoleRepository userRoleRepository, UserRepository userRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRoleRepository.count() == 0) {
            for (Role role : Role.values()) {
                userRoleRepository.saveAndFlush(new UserRole(role));
            }
        }
    }
}
