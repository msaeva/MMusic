package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, String> {
    Optional<Song> findByUuid(String songUuid);
}


