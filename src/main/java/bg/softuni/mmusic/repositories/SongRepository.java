package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.Style;
import bg.softuni.mmusic.model.enums.SongStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, String> {
    Optional<Song> findByUuid(String songUuid);

    Page<Song> findAllByStatus(SongStatus status, Pageable pageable);

    @Query(value = "select s from Song s where s.uuid not in (select song.uuid from Song song " +
            "join PlaylistSongs ps on song.uuid = ps.song.uuid " +
            "where ps.playlist.uuid = :playlistUuid " +
            " and song.status = :status)")
    List<Song> findAllNotInPlaylist(@Param("playlistUuid") String playlistUuid, @Param("status") SongStatus status);

    Optional<List<Song>> findAllByAuthorUuid(String uuid);

    Optional<List<Song>> findAllByAuthorUuidAndStatus(String uuid, SongStatus status);

    Page<Song> getByStatusOrderByLikes(SongStatus status, Pageable pageable);

    @Query("select s from Song as s " +
            "where s.Style.uuid = :styleUuid")
    Page<Song> findAllByStyleUuid(@Param(value = "styleUuid") String styleUuid, Pageable pageable);
}




