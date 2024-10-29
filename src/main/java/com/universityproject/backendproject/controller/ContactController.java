package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.contact.request.ContactRequest;
import com.universityproject.backendproject.service.contact.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<Void> sendContactForm(@RequestBody ContactRequest request) {
        contactService.sendEmail(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
