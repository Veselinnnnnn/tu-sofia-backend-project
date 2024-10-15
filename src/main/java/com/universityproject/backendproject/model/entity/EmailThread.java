package com.universityproject.backendproject.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "email_thread")
@NoArgsConstructor
@Getter
@Setter
public class EmailThread extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact; // Corrected: One-to-One relationship

    private String userEmail;
    private String adminEmail;
    private String subject;

    @OneToMany(mappedBy = "emailThread", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailMessage> messages = new ArrayList<>();
}

