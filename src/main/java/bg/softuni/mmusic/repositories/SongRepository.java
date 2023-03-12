package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.enums.SongStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, String> {
    Optional<Song> findByUuid(String songUuid);

    Page<Song> findAllByStatus(SongStatus status, Pageable pageable);

    Optional<List<Song>> findAllByAuthorUuid(String uuid);

    Optional<List<Song>> findAllByAuthorUuidAndStatus(String uuid, SongStatus status);
}




