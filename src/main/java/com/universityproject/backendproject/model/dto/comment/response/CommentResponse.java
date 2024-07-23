package com.universityproject.backendproject.model.dto.comment.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CommentResponse {

    private String author;

    private String description;
}
