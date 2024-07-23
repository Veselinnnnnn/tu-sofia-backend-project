package com.universityproject.backendproject.service.comment;

import com.universityproject.backendproject.model.dto.comment.response.CommentResponse;

import java.util.List;

public interface CommentService {

    void createComment(Long authorId, String description, Long userId);

    List<CommentResponse> findAllByUserId(Long id);
}
