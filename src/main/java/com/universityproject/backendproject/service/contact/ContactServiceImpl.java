package com.universityproject.backendproject.service.contact;

import com.universityproject.backendproject.model.dto.contact.request.ContactRequest;
import com.universityproject.backendproject.model.dto.contact.request.ReplyRequest;
import com.universityproject.backendproject.model.dto.contact.response.ContactResponse;
import com.universityproject.backendproject.model.dto.contact.response.EmailMessageResponse;
import com.universityproject.backendproject.model.dto.contact.response.EmailThreadResponse;
import com.universityproject.backendproject.model.entity.Contact;
import com.universityproject.backendproject.model.entity.EmailMessage;
import com.universityproject.backendproject.model.entity.EmailThread;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.repository.ContactRepository;
import com.universityproject.backendproject.repository.EmailThreadRepository;
import com.universityproject.backendproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final ModelMapper modelMapper;
    private final EmailThreadRepository emailThreadRepository;

    @Override
    public void sendEmail(ContactRequest request) {
        Optional<User> optional = userRepository.findById(1L);
        if (optional.isPresent()) {
            User adminUser = optional.get();

            Contact contact = new Contact();
            contact.setName(request.getName());
            contact.setEmail(request.getEmail());
            contact.setPhone(request.getPhone());
            contact.setSubject(request.getSubject());
            contact.setMessage(request.getMessage());

            contactRepository.save(contact);
            Long savedId = contact.getId();

            EmailThread emailThread = new EmailThread();
            emailThread.setUserEmail(contact.getEmail());
            emailThread.setAdminEmail(adminUser.getEmail());
            emailThread.setSubject(contact.getSubject());

            EmailMessage initialMessage = new EmailMessage();
            initialMessage.setEmailThread(emailThread);
            initialMessage.setSender(request.getEmail());
            initialMessage.setMessage(contact.getMessage());
            initialMessage.setSentAt(LocalDateTime.now());

            emailThread.getMessages().add(initialMessage);
            emailThread.setContact(contact);
            emailThreadRepository.save(emailThread);

            String replyUrl = "http://localhost:4200/contact-us/contact-reply?id=" + savedId;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminUser.getEmail());
            message.setSubject("New Contact Form Submission: " + request.getSubject());

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Hello Admin,\n\n")
                    .append("You have received a new contact form submission. Here are the details:\n\n")
                    .append("Name: ").append(contact.getName()).append("\n")
                    .append("Email: ").append(contact.getEmail()).append("\n")
                    .append("Phone: ").append(contact.getPhone() != null ? contact.getPhone() : "Not provided").append("\n")
                    .append("Subject: ").append(contact.getSubject()).append("\n")
                    .append("Message:\n").append(contact.getMessage()).append("\n")
                    .append("To reply to the sender, click the link below:\n")
                    .append(replyUrl).append("\n")
                    .append("Best regards,\nYour Website Team");

            message.setText(emailContent.toString());
            mailSender.send(message);
        } else {
            throw new RuntimeException("Admin user not found. Cannot send email.");
        }
    }

    @Override
    public void sendReply(ReplyRequest request) {
        Optional<User> adminUser = this.userRepository.findById(1L); // Assuming admin user ID is 1L

        if (adminUser.isPresent()) {
            Optional<EmailThread> emailThreadOptional = this.emailThreadRepository.findById(request.getThreadId());
            if (emailThreadOptional.isEmpty()) {
                throw new RuntimeException("Email thread not found. Cannot update chat.");
            }

            EmailThread emailThread = emailThreadOptional.get();

            // Create a new message
            EmailMessage newMessage = new EmailMessage();
            newMessage.setEmailThread(emailThread);
            newMessage.setMessage(request.getMessage());
            newMessage.setSentAt(LocalDateTime.now());

            // Check the last message to determine the sender and receiver
            List<EmailMessage> messages = emailThread.getMessages();
            if (!messages.isEmpty()) {
                EmailMessage lastMessage = messages.get(messages.size() - 1);
                String lastSenderEmail = lastMessage.getSender(); // Get the last sender's email

                // Swap sender and receiver based on the last message
                if (lastSenderEmail.equals(emailThread.getUserEmail())) {
                    // If the last message was sent by the user, admin replies
                    newMessage.setSender(adminUser.get().getEmail()); // Admin is sending the reply
                    newMessage.setReceiver(emailThread.getUserEmail()); // Send to the original sender (user)
                } else {
                    // If the last message was sent by the admin, user replies
                    newMessage.setSender(emailThread.getUserEmail()); // User is sending the reply
                    newMessage.setReceiver(adminUser.get().getEmail()); // Admin is receiving the reply
                }
            } else {
                // If there are no messages, set default sender and receiver
                newMessage.setSender(adminUser.get().getEmail()); // Default to admin
                newMessage.setReceiver(emailThread.getUserEmail()); // Original sender
            }

            // Add the new message to the thread
            emailThread.getMessages().add(newMessage);
            emailThreadRepository.save(emailThread);

            // Prepare the reply email message
            SimpleMailMessage replyMessage = new SimpleMailMessage();
            replyMessage.setFrom(newMessage.getSender()); // Set from the determined sender
            replyMessage.setTo(request.getTo()); // Set to the determined receiver
            replyMessage.setSubject(request.getSubject());

            // Build the reply URL
            String replyUrl = "http://localhost:4200/contact-us/contact-reply?threadId=" + request.getThreadId();

            // Construct the email content
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Hello,\n\n")
                    .append("You have received a reply regarding your previous contact:\n\n")
                    .append("----------------------------------------------------\n")
                    .append("Subject: ").append(request.getSubject()).append("\n")
                    .append("Message:\n").append(request.getMessage()).append("\n")
                    .append("----------------------------------------------------\n\n")
                    .append("To reply, please click the link below:\n")
                    .append(replyUrl).append("\n\n")
                    .append("This is an automatic notification sent by the website's contact system.\n")
                    .append("You can continue the conversation using the above link.\n\n")
                    .append("Best regards,\n")
                    .append("Your Website Team");

            replyMessage.setText(emailContent.toString());

            // Send the email
            mailSender.send(replyMessage);
        } else {
            throw new RuntimeException("Admin user not found. Cannot send email.");
        }
    }


    @Override
    public ContactResponse getContact(Long id) {
        Optional<Contact> contactOpt = contactRepository.findById(id);
        if (contactOpt.isPresent()) {
            Contact contact = contactOpt.get();
            // Map Contact to ContactResponse
            return new ModelMapper().map(contact, ContactResponse.class);
        }
        throw new RuntimeException("Contact not found");
    }

    @Override
    public EmailThreadResponse getEmailThreadById(Long threadId) {
        EmailThread emailThread = emailThreadRepository.findById(threadId)
                .orElseThrow(() -> new RuntimeException("Email thread not found"));

        // Map EmailThread entity to EmailThreadResponse DTO
        EmailThreadResponse response = modelMapper.map(emailThread, EmailThreadResponse.class);

        // Manually map the list of EmailMessage entities to EmailMessageResponse DTOs
        response.setMessages(
                emailThread.getMessages().stream()
                        .map(message -> modelMapper.map(message, EmailMessageResponse.class))
                        .collect(Collectors.toList())
        );

        return response;
    }
}
