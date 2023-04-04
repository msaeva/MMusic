package bg.softuni.mmusic.init;

import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.repositories.UserRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleSeeder {
    private final UserRoleRepository userRoleRepository;

    public RoleSeeder(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public void init() {
        if (userRoleRepository.count() == 0) {
            for (Role role : Role.values()) {
                userRoleRepository.saveAndFlush(new UserRole(role));
            }
        }
    }
}
