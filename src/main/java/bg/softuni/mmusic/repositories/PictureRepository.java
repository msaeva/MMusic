package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<Picture, String> {


    @Query("select url from Picture " +
            "where uuid = :uuid")
    String getUrlByUuid(@Param("uuid") String uuid);

}
