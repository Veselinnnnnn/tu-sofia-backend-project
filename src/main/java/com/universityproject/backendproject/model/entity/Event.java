package com.universityproject.backendproject.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@NoArgsConstructor
@Getter
@Setter
public class Event extends BaseEntity {
    private String title;
    private String description;
    private LocalDateTime dateTime;

    public boolean isUpcoming() {
        return dateTime.isAfter(LocalDateTime.now());
    }
}
