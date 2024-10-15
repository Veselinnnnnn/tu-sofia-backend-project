package com.universityproject.backendproject.service.contact;

import com.universityproject.backendproject.model.dto.contact.request.ContactRequest;
import com.universityproject.backendproject.model.dto.contact.request.ReplyRequest;
import com.universityproject.backendproject.model.dto.contact.response.ContactResponse;
import com.universityproject.backendproject.model.dto.contact.response.EmailThreadResponse;

public interface ContactService {
    void sendEmail(ContactRequest request);
    void sendReply(ReplyRequest request);
    ContactResponse getContact(Long id);
    EmailThreadResponse getEmailThreadById(Long threadId);
}
