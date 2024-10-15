package com.universityproject.backendproject.model.dto.application.request;

import com.universityproject.backendproject.model.enums.ApplicationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
@Getter
@Setter
public class ApplicationRequest {
    private Long userId;
    private String name;
    private String email;
    private ApplicationType requestType;
    private Long animalId;
    private LocalTime pickUpTime;
    private LocalTime returnTime;
    private LocalDate date;
}