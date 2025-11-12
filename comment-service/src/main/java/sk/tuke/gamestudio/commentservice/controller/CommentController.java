package sk.tuke.gamestudio.commentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.commentservice.entity.Comment;
import sk.tuke.gamestudio.commentservice.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        Comment savedComment = commentService.addComment(comment.getPlayer(), comment.getComment());
        return ResponseEntity.ok(savedComment);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllRatings();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getCommentCount() {
        long count = commentService.getCommentCount();
        return ResponseEntity.ok(count);
    }

    @DeleteMapping
    public ResponseEntity<Void> resetComments() {
        commentService.reset();
        return ResponseEntity.noContent().build();
    }
}
