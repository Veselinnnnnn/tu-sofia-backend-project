package com.universityproject.backendproject.model.dto.contact.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class ReplyRequest {
    private String to;
    private String subject;
    private String message;
    private Long threadId;
}
