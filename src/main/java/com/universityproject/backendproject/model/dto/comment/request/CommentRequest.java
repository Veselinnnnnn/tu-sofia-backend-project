package com.universityproject.backendproject.model.dto.comment.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CommentRequest {
    private Long authorId;

    private String description;

    private Long userId;
}
