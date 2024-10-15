package com.universityproject.backendproject.model.dto.comment.response;

import com.universityproject.backendproject.model.enums.ReactionTypeEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class CommentResponse {
    private Long id;
    private Long userId;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private Long animalId;
    private int likes;
    private int dislikes;
    private int rating;
    private ReactionTypeEnum currentUserReactionType;
}
