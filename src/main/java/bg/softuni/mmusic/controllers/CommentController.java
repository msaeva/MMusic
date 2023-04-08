package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.dtos.comment.CommentDto;
import bg.softuni.mmusic.model.dtos.comment.CreateCommentDto;
import bg.softuni.mmusic.model.entities.Comment;
import bg.softuni.mmusic.model.entities.User;
import bg.softuni.mmusic.model.enums.Role;
import bg.softuni.mmusic.services.AuthService;
import bg.softuni.mmusic.services.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class CommentController {
    private final CommentService commentService;
    private final AuthService authService;

    @Autowired
    public CommentController(CommentService commentService, AuthService authService) {
        this.commentService = commentService;
        this.authService = authService;
    }
    /**
     * { @code GET / comments}: retrieves all comments by song uuid (specified in the request as params)
     * @return return ResponseEntity<List<CommentDto>>
     * */
    @GetMapping("song/{songUuid}/comments")
    public ResponseEntity<List<CommentDto>> getSongComments(@PathVariable(name = "songUuid") String songUuid) {
        List<Comment> commentsBySong = commentService.getCommentsBySong(songUuid);

        List<CommentDto> comments =
                commentsBySong.stream().map(commentService::toCommentDto).collect(Collectors.toList());

        return ResponseEntity.ok(comments);
    }

    @PostMapping("song/{songUuid}/comments")
    public ResponseEntity<CommentDto> createComment(@RequestBody CreateCommentDto commentDto,
                                                    @PathVariable(name = "songUuid") String songUuid) {

        CommentDto comment = commentService.createComment(commentDto, songUuid);

        return ResponseEntity.created(URI.create(String.format("song/%s/comments/%s", songUuid, comment.getUuid()))).body(comment);
    }

    @GetMapping("song/{songUuid}/comments/{commentUuid}")
    public ResponseEntity<CommentDto> getComment(@PathVariable(name = "commentUuid") String commentUuid) {
        return ResponseEntity.ok(commentService.getComment(commentUuid));
    }

    @DeleteMapping("song/{songUuid}/comments/{commentUuid}/delete")
    public HttpStatus deleteComment(@PathVariable(name = "commentUuid") String commentUuid,
                                    @PathVariable(name = "songUuid") String songUuid) {

        User authUser = authService.getAuthenticatedUser();
        Comment comment = commentService.getCommentByUuid(commentUuid);

        if (comment.getAuthor().getUuid().equals(authUser.getUuid()) ||
                authUser.getRoles().stream().anyMatch(r -> r.getRole().equals(Role.ADMIN))) {

            commentService.deleteComment(comment);
            log.info("Deleted comment {}.", comment);
            return HttpStatus.OK;
        }
        return HttpStatus.valueOf(403);
    }
}
