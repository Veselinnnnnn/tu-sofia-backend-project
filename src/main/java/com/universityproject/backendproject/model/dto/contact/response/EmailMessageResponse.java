package com.universityproject.backendproject.model.dto.contact.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Data
@Getter
@Setter
public class EmailMessageResponse {
    private Long messageId;
    private String sender;
    private String receiver;
    private String message;
    private LocalDateTime sentAt;

    // Getters and Setters
}