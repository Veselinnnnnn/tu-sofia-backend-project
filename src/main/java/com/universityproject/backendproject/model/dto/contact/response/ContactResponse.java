package com.universityproject.backendproject.model.dto.contact.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class ContactResponse {
    private String name;
    private String email;
    private String phone;
    private String subject;
    private String message;
    private Long threadId;
}
