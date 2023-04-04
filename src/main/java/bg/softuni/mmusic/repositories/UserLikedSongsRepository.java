package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.UserLikedSongs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLikedSongsRepository extends JpaRepository<UserLikedSongs, String> {
    @Query("select u from UserLikedSongs  as u " +
            "where u.user_uuid = :userUuid and u.song_uuid= :songUuid")
    UserLikedSongs getBySongAndUser(@Param("userUuid") String userUuid,
                                    @Param("songUuid") String songUuid);


    @Query("select song_uuid from UserLikedSongs " +
            "where user_uuid = :userUuid")
    List<String> getUserLikedSongs(@Param("userUuid") String uuid);

    @Query("select u from UserLikedSongs  as u " +
            "where u.song_uuid= :songUuid")
    List<UserLikedSongs> getAllBySong(@Param("songUuid") String songUuid);
}
