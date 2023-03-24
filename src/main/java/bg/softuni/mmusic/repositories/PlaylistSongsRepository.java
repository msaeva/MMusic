package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.PlaylistSongs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistSongsRepository extends JpaRepository<PlaylistSongs, String> {

    PlaylistSongs getByPlaylistUuidAndSongUuid(String uuid, String uuid1);
}

