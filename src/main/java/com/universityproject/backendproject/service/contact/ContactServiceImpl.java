package com.universityproject.backendproject.service.contact;

import com.universityproject.backendproject.exception.AdminUserNotFoundException;
import com.universityproject.backendproject.model.dto.contact.request.ContactRequest;
import com.universityproject.backendproject.model.entity.Contact;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.repository.ContactRepository;
import com.universityproject.backendproject.repository.UserRepository;
import com.universityproject.backendproject.service.jwt.JwtServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {

    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(ContactRequest request) {
        log.info("Starting to send email for contact request from: {}", request.getEmail());

        Optional<User> optionalAdmin = userRepository.findById(1L);
        User adminUser = optionalAdmin.orElseThrow(() -> {
            log.error("Admin user not found. Cannot send email.");
            return new AdminUserNotFoundException("Admin user not found. Cannot send email.");
        });

        Contact contact = new Contact();
        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setSubject(request.getSubject());
        contact.setMessage(request.getMessage());

        contactRepository.save(contact);
        log.info("Contact saved successfully for email: {}", request.getEmail());

        sendEmailToAdmin(adminUser.getEmail(), contact);
    }

    private void sendEmailToAdmin(String adminEmail, Contact contact) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(adminEmail);
        message.setSubject("New Contact Form Submission: " + contact.getSubject());

        String emailContent = String.format("Hello Admin,\n\n" +
                        "You have received a new contact form submission. Here are the details:\n\n" +
                        "Name: %s\n" +
                        "Email: %s\n" +
                        "Phone: %s\n" +
                        "Subject: %s\n" +
                        "Message:\n%s\n\n" +
                        "Best regards,\nYour Website Team",
                contact.getName(),
                contact.getEmail(),
                contact.getPhone() != null ? contact.getPhone() : "Not provided",
                contact.getSubject(),
                contact.getMessage());

        message.setText(emailContent);
        mailSender.send(message);
        log.info("Email sent to admin: {}", adminEmail);
    }
}
