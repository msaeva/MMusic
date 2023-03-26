package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.enums.PlaylistStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, String> {
    Optional<List<Playlist>> findAllByOwnerUuid(String uuid);

    Optional<List<Playlist>> findAllByOwnerUuidAndStatus(String uuid, PlaylistStatus aPublic);

    Optional<Playlist> findByUuid(String playlistUuid);
}

