package com.universityproject.backendproject.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contact")
@NoArgsConstructor
@Getter
@Setter
public class Contact extends BaseEntity {
    private String name;
    private String email;
    private String phone;
    private String subject;
    private String message;
    @OneToOne(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private EmailThread emailThread;
}

