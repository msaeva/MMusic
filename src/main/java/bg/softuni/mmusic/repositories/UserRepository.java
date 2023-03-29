package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.Song;
import bg.softuni.mmusic.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUuid(String uuid);


    @Query("select u.username from User as u " +
            "where u.uuid = :uuid")
    String getUsernameByUuid(@Param("uuid") String uuid);

    @Query("select s from Song as s "+
            "join UserFavouriteSongs ufs on s.uuid = ufs.song_uuid " +
            "where ufs.user_uuid = :userUuid")
    List<Song> getUserFavouriteSongs(@Param("userUuid") String userUuid);
}

