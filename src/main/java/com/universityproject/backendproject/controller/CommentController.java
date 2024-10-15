package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.comment.request.CommentRequest;
import com.universityproject.backendproject.model.dto.comment.response.CommentResponse;
import com.universityproject.backendproject.model.entity.Comment;
import com.universityproject.backendproject.service.comment.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<Void> addComment(
            @RequestParam Long animalId,
            @RequestParam Long userId,
            @RequestBody CommentRequest request
    ) {
        this.commentService.addComment(animalId, userId, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<CommentResponse>> getCommentsByAnimalId(@RequestParam Long animalId) {
        List<CommentResponse> comments = this.commentService.findCommentsByAnimalId(animalId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<Void> updateComment(@RequestParam Long commentId, @RequestBody Comment request) {
        this.commentService.updateComment(commentId, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteComment(@RequestParam Long commentId) {
        this.commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/like")
    public ResponseEntity<CommentResponse> incrementLikes(@RequestParam Long commentId, @RequestParam Long userId) {
        CommentResponse updatedComment = this.commentService.incrementLikes(commentId, userId);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @PutMapping("/dislike")
    public ResponseEntity<CommentResponse> incrementDislikes(@RequestParam Long commentId, @RequestParam Long userId) {
        CommentResponse updatedComment = this.commentService.incrementDislikes(commentId, userId);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }
}
