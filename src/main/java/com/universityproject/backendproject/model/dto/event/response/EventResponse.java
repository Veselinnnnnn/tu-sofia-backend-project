package com.universityproject.backendproject.model.dto.event.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Data
@Getter
@Setter
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dateTime;
}
