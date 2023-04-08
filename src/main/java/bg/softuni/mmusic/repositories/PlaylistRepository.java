package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.Playlist;
import bg.softuni.mmusic.model.enums.PlaylistStatus;
import bg.softuni.mmusic.model.enums.SongStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, String> {
    Optional<List<Playlist>> findAllByOwnerUuid(String uuid);

    Optional<List<Playlist>> findAllByOwnerUuidAndStatus(String uuid, PlaylistStatus aPublic);

    Optional<Playlist> findByUuid(String playlistUuid);

    @Query("select p from Playlist as p " +
            " left join PlaylistSongs ps on ps.playlist.uuid = p.uuid " +
            " left join Song s on s.uuid = ps.song.uuid" +
            " where s.status = :status "+
            " group by p.uuid " +
            "order by sum(s.likes) desc")
    List<Playlist> getTopPlaylists(@Param("status") SongStatus status);
}

