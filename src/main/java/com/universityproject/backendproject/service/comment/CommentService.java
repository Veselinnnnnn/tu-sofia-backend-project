package com.universityproject.backendproject.service.comment;

import com.universityproject.backendproject.model.dto.comment.request.CommentRequest;
import com.universityproject.backendproject.model.dto.comment.response.CommentResponse;
import com.universityproject.backendproject.model.entity.Comment;
import com.universityproject.backendproject.model.entity.User;

import java.util.List;

public interface CommentService {

//    void createComment(Long authorId, String description, Long userId);

    void addComment(Long animalId, Long userId, CommentRequest request);

    void deleteComment(Long id);

    void updateComment(Long id, Comment updatedComment);

    List<CommentResponse> findCommentsByAnimalId(Long animalId);

    CommentResponse incrementLikes(Long commentId, Long userId);
    CommentResponse incrementDislikes(Long commentId, Long userId);

//    List<CommentResponse> findAllByUserId(Long id);
}
