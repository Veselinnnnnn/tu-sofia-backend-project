package com.universityproject.backendproject.model.dto.application.response;

import com.universityproject.backendproject.model.enums.ApplicationStatus;
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
public class ApplicationResponse {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private ApplicationType requestType;
    private ApplicationStatus status;
    private Long animalId;
    private LocalTime pickUpTime;
    private LocalTime returnTime;
    private LocalDate date;
}
