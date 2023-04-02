package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.Style;
import bg.softuni.mmusic.model.enums.StyleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StyleRepository extends JpaRepository<Style, String> {
    Optional<Style> findByType(StyleType type);

    Optional<Style> findByUuid(String uuid);
}
