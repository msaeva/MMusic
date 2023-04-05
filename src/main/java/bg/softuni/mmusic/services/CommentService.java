package bg.softuni.mmusic.services;

import bg.softuni.mmusic.model.dtos.comment.CommentDto;
import bg.softuni.mmusic.model.dtos.comment.CreateCommentDto;
import bg.softuni.mmusic.model.entities.Comment;
import bg.softuni.mmusic.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentService {
    private final SongService songService;
    private final AuthService authService;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(SongService songService, AuthService authService, CommentRepository commentRepository) {
        this.songService = songService;
        this.authService = authService;
        this.commentRepository = commentRepository;
    }

    public CommentDto createComment(CreateCommentDto commentDto, String songUuid) {

        Comment comment = new Comment()
                .setText(commentDto.getText())
                .setSong(songService.findSongByUuid(songUuid))
                .setCreated(LocalDateTime.now())
                .setAuthor(authService.getAuthenticatedUser());

        commentRepository.saveAndFlush(comment);

        return toCommentDto(comment);
    }

    public CommentDto toCommentDto(Comment comment) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        return CommentDto.builder()
                .uuid(comment.getUuid())
                .text(comment.getText())
                .username(comment.getAuthor().getUsername())
                .createdDate(comment.getCreated().format(format))
                .build();

    }

    public List<Comment> getCommentsBySong(String songUuid) {
        return commentRepository.findAllBySong(songService.findSongByUuid(songUuid));
    }

    public CommentDto getComment(String commentUuid) {
        Comment comment = commentRepository.findByUuid(commentUuid).orElseThrow(NoSuchElementException::new);
        return toCommentDto(comment);
    }

    public Comment getCommentByUuid(String commentUuid) {
        return commentRepository.findByUuid(commentUuid).orElseThrow(NoSuchElementException::new);
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
