package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.contact.request.ContactRequest;
import com.universityproject.backendproject.model.dto.contact.request.ReplyRequest;
import com.universityproject.backendproject.model.dto.contact.response.ContactResponse;
import com.universityproject.backendproject.model.dto.contact.response.EmailThreadResponse;
import com.universityproject.backendproject.service.contact.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public void sendContactForm(@RequestBody ContactRequest request) {
        this.contactService.sendEmail(request);
    }

    @PostMapping("/reply")
    public void replyToContact(@RequestBody ReplyRequest request) {
        this.contactService.sendReply(request);
    }

    @GetMapping("/{id}")
    public ContactResponse getContactDetails(@PathVariable Long id) {
        return contactService.getContact(id);
    }

    @GetMapping("/threads/{threadId}")
    public EmailThreadResponse getEmailThread(@PathVariable Long threadId) {
        return contactService.getEmailThreadById(threadId);
    }
}
