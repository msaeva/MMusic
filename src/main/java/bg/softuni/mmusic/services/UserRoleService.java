package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.repositories.UserRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public List<UserRole> findAll() {
       return userRoleRepository.findAll();
    }
}
