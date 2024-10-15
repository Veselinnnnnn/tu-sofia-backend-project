package com.universityproject.backendproject.repository;

import com.universityproject.backendproject.model.entity.Comment;
import com.universityproject.backendproject.model.entity.CommentReaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
    Optional<CommentReaction> findByUserIdAndCommentId(Long userId, Long commentId);
}