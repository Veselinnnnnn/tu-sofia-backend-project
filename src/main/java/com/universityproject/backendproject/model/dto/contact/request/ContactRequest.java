package com.universityproject.backendproject.model.dto.contact.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class ContactRequest {
    private String name;
    private String email;
    private String phone;
    private String subject;
    private String message;
}
