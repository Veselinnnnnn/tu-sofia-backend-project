package com.universityproject.backendproject.service.contact;

import com.universityproject.backendproject.model.dto.contact.request.ContactRequest;

public interface ContactService {
    void sendEmail(ContactRequest request);
}
