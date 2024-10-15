package com.universityproject.backendproject.model.dto.testimonial.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Data
@Getter
@Setter
public class TestimonialRequest {
    private String message;
    private Long userId;
}
