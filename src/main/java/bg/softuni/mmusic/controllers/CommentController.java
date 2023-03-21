package bg.softuni.mmusic.controllers;

import bg.softuni.mmusic.model.dtos.comment.CommentDto;
import bg.softuni.mmusic.model.dtos.comment.CreateCommentDto;
import bg.softuni.mmusic.model.entities.Comment;
import bg.softuni.mmusic.services.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

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
        log.info(songUuid);
        log.info(String.valueOf(commentDto));
        System.out.println(songUuid);
        System.out.println(commentDto.getText());

        CommentDto comment = commentService.createComment(commentDto, songUuid);

        return ResponseEntity.created(URI.create(String.format("song/%s/comments/%s", songUuid, comment.getUuid()))).body(comment);
    }

    @GetMapping("song/{songUuid}/comments/{commentUuid}")
    public ResponseEntity<CommentDto> getComment(@PathVariable(name = "commentUuid") String commentUuid) {
        return ResponseEntity.ok(commentService.getCommentByUuid(commentUuid));
    }
}
