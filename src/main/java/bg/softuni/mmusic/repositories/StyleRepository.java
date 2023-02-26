package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.entities.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StyleRepository extends JpaRepository<Style, String> {
}
