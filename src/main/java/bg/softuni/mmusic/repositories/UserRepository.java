package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}

