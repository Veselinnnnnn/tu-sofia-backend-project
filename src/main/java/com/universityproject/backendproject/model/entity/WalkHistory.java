package com.universityproject.backendproject.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "walk_histories")
@Getter
@Setter
public class WalkHistory extends BaseEntity {

    @ManyToOne
    private User user;

    @Column
    private String animalName;

    @Column
    private String animalType;

    @Column
    private LocalDate localDate;
}
