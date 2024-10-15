package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.comment.request.CommentRequest;
import com.universityproject.backendproject.model.dto.comment.response.CommentResponse;
import com.universityproject.backendproject.model.entity.Comment;
import com.universityproject.backendproject.service.comment.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public void addComment(
            @RequestParam Long animalId,
            @RequestParam Long userId,
            @RequestBody CommentRequest request
    ) {
        this.commentService.addComment(animalId, userId, request);
    }

    @GetMapping("")
    public List<CommentResponse> getCommentsByAnimalId(@RequestParam Long animalId) {
        return this.commentService.findCommentsByAnimalId(animalId);
    }

    @PutMapping("")
    public void updateComment(@RequestParam Long commentId, @RequestBody Comment request) {
        this.commentService.updateComment(commentId, request);
    }

    @DeleteMapping("")
    public void deleteComment(@RequestParam Long commentId) {
        this.commentService.deleteComment(commentId);
    }

    @PutMapping("/like")
    public CommentResponse incrementLikes(@RequestParam Long commentId, @RequestParam Long userId) {
        return this.commentService.incrementLikes(commentId, userId);
    }

    @PutMapping("/dislike")
    public CommentResponse incrementDislikes(@RequestParam Long commentId, @RequestParam Long userId) {
        return this.commentService.incrementDislikes(commentId, userId);
    }
}
