package bg.softuni.mmusic.repositories;

import bg.softuni.mmusic.model.entities.Comment;
import bg.softuni.mmusic.model.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllBySong(Song song);

    Optional<Comment> findByUuid(String commentUuid);
}
