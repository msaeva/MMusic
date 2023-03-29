package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.UserFavouriteSongs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavouriteSongsRepository extends JpaRepository<UserFavouriteSongs, String> {

    @Query("select song_uuid from UserFavouriteSongs " +
            "where user_uuid = :userUuid")
    List<String> getUserFavouriteSongs(@Param("userUuid") String uuid);

    @Query("select u  from UserFavouriteSongs as u " +
            "where u.user_uuid = :userUuid and u.song_uuid= :songUuid")
    UserFavouriteSongs getBySongAndUser(@Param("userUuid") String userUuid,
                                                  @Param("songUuid") String songUuid);
}
