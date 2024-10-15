package com.universityproject.backendproject.model.entity;

import com.universityproject.backendproject.model.enums.ApplicationStatus;
import com.universityproject.backendproject.model.enums.ApplicationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "application")
@NoArgsConstructor
@Getter
@Setter
public class Application extends BaseEntity {
    private String name;
    private String email;
    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    private ApplicationType requestType;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    private LocalTime pickUpTime;
    private LocalTime returnTime;
    private LocalDate date;
}
