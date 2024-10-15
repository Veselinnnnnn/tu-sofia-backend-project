package com.universityproject.backendproject.model.dto.event.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Data
@Getter
@Setter
public class EventRequest {
    private String title;
    private String description;
    private LocalDateTime dateTime;
}
