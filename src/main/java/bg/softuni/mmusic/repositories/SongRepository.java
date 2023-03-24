package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.enums.SongStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SongRepository extends JpaRepository<Song, String> {
    Optional<Song> findByUuid(String songUuid);

    Page<Song> findAllByStatus(SongStatus status, Pageable pageable);

    Set<Song> findAllByStatus(SongStatus status);

//    @Query(value = "select s from songs s where s.uuid not in (select song.uuid from Song song\n" +
//            "join playlists_songs ps on song.uuid = ps.songs_uuid\n" +
//            "where ps.playlist_uuid = :playlistUuid " +
//            " and song.status = :status)", nativeQuery = true)
//    List<Song> findAllNotInPlaylist(@Param("playlistUuid") String playlistUuid, @Param("status") SongStatus status);

    Optional<List<Song>> findAllByAuthorUuid(String uuid);

    Optional<List<Song>> findAllByAuthorUuidAndStatus(String uuid, SongStatus status);

}




