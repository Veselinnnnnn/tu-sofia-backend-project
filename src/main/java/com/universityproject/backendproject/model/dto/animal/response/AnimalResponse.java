package com.universityproject.backendproject.model.dto.animal.response;

import com.universityproject.backendproject.model.dto.comment.response.CommentResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class AnimalResponse {
    private Long id; // Assuming `BaseEntity` has an `id` field
    private String name;
    private String type;
    private boolean availability;
    private String breed;
    private int age;
    private String description;
    private double rating;
    private String slogan;
    private byte[] img; // Consider excluding this if the image is too large
    private Long userId; // Exposing only the user ID
    private List<CommentResponse> comments;
}


