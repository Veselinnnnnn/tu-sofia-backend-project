package com.universityproject.backendproject.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "animals")
@NoArgsConstructor
@Getter
@Setter
public class Animal extends BaseEntity {

    @Column
    private String name;

    @Column
    private String type;

    @Column
    private boolean availability;

    @Column
    private String breed;

    @Column
    private int age = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private double rating = 0.0;

    @Column
    private String slogan;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] img;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Application> applications;
}
