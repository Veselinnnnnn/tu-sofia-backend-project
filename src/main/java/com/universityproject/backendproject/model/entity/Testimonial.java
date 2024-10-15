package com.universityproject.backendproject.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "testimonials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Testimonial extends BaseEntity {
    @Column(nullable = false)
    private String message; // The testimonial message

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt; // Timestamp when the testimonial was created

    @ManyToOne // Assuming many testimonials can belong to one user
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User who left the testimonial
}