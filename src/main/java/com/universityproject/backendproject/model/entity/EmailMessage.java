package com.universityproject.backendproject.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@NoArgsConstructor
@Getter
@Setter
public class EmailMessage extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "thread_id", nullable = false)
    private EmailThread emailThread;

    private String sender;
    private String receiver;
    private String message;
    @Column(nullable = false, updatable = false)
    private LocalDateTime sentAt = LocalDateTime.now(); // Automatically set to current time

}
