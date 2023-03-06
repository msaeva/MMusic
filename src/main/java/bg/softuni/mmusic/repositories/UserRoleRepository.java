package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.UserRole;
import bg.softuni.mmusic.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRole(Role role);
}
