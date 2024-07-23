package com.universityproject.backendproject.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToOne
    private User user;
}
