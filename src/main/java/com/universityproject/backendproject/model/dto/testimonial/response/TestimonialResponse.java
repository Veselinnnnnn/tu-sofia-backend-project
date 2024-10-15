package com.universityproject.backendproject.model.dto.testimonial.response;

import com.universityproject.backendproject.model.entity.User;
import lombok.*;

import java.util.Date;


@Data
@Getter
@Setter
public class TestimonialResponse {
    private Long id; // Unique identifier for the testimonial
    private String message; // The testimonial message
    private Date createdAt; // Timestamp when the testimonial was created
    private User user; // User object containing user details
}
