package com.universityproject.backendproject.model.dto.contact.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
@Getter
@Setter
public class EmailThreadResponse {
    private Long threadId;
    private String userEmail;
    private String adminEmail;
    private String subject;
    private List<EmailMessageResponse> messages;
}