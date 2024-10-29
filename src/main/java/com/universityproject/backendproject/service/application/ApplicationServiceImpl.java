package com.universityproject.backendproject.service.application;


import com.universityproject.backendproject.model.dto.application.request.ApplicationCompositeRequest;
import com.universityproject.backendproject.model.dto.application.request.ApplicationDetailsRequest;
import com.universityproject.backendproject.model.dto.application.request.ApplicationRequest;
import com.universityproject.backendproject.model.dto.application.response.ApplicationResponse;
import com.universityproject.backendproject.model.entity.Animal;
import com.universityproject.backendproject.model.entity.Application;
import com.universityproject.backendproject.model.entity.ApplicationDetails;
import com.universityproject.backendproject.model.entity.User;
import com.universityproject.backendproject.model.enums.ApplicationStatus;
import com.universityproject.backendproject.repository.AnimalRepository;
import com.universityproject.backendproject.repository.ApplicationDetailsRepository;
import com.universityproject.backendproject.repository.ApplicationRepository;
import com.universityproject.backendproject.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationDetailsRepository applicationDetailsRepository;
    private final ApplicationRepository applicationRepository;
    private final AnimalRepository animalRepository;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public ApplicationResponse create(ApplicationCompositeRequest  request) {
        Application application = new Application();
        application.setName(request.getApplicationRequest().getName());
        application.setEmail(request.getApplicationRequest().getEmail());
        application.setRequestDate(LocalDateTime.now());
        application.setStatus(ApplicationStatus.PENDING);
        application.setRequestType(request.getApplicationRequest().getRequestType());
        application.setPickUpTime(request.getApplicationRequest().getPickUpTime());
        application.setReturnTime(request.getApplicationRequest().getReturnTime());
        application.setDate(request.getApplicationRequest().getDate());

        User user = this.userRepository.findById(request.getApplicationRequest().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        application.setUser(user);

        Animal animal = this.animalRepository.findById(request.getApplicationRequest().getAnimalId())
                .orElseThrow(() -> new IllegalArgumentException("Animal not found"));
        application.setAnimal(animal);

        Application savedRequest = this.applicationRepository.save(application);

        handleApplicationDetails(savedRequest.getId(), request.getApplicationDetailsRequest());

        sendCreateApplicationEmail(savedRequest);

        return this.modelMapper.map(savedRequest, ApplicationResponse.class);
    }

    @Override
    public ApplicationResponse findById(Long applicationId) {
        Application application = this.applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + applicationId));

        return this.modelMapper.map(application, ApplicationResponse.class);
    }

    @Override
    public void updateStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Application> applications = this.applicationRepository.findAll();
        for (Application app : applications) {
            if (app.getReturnTime().isBefore(LocalTime.from(now))) {
                app.setStatus(ApplicationStatus.OUTDATED);
                this.applicationRepository.save(app);
            }
        }
    }

    @Override
    public void delete(Long applicationId) {
        Optional<Application> application = this.applicationRepository.findById(applicationId);
        application.ifPresent(this.applicationRepository::delete);
    }

    @Override
    public void update(Long applicationId, ApplicationCompositeRequest request) {
        Application application = this.applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        application.setName(request.getApplicationRequest().getName());
        application.setEmail(request.getApplicationRequest().getEmail());
        application.setRequestType(request.getApplicationRequest().getRequestType());
        application.setPickUpTime(request.getApplicationRequest().getPickUpTime());
        application.setReturnTime(request.getApplicationRequest().getReturnTime());
        application.setDate(request.getApplicationRequest().getDate());

        User user = this.userRepository.findById(request.getApplicationRequest().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        application.setUser(user);

        Animal animal = this.animalRepository.findById(request.getApplicationRequest().getAnimalId())
                .orElseThrow(() -> new IllegalArgumentException("Animal not found"));
        application.setAnimal(animal);

        this.applicationRepository.save(application);

        handleApplicationDetails(application.getId(), request.getApplicationDetailsRequest());
    }

    @Override
    public List<ApplicationResponse> getApplicationsByUserId(Long userId) {
        List<Application> applications = this.applicationRepository.findByUserId(userId);

        return applications.stream()
                .map(application -> this.modelMapper.map(application, ApplicationResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public ApplicationResponse approveApplication(Long applicationId) {
        Application application = this.applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + applicationId));

        application.setStatus(ApplicationStatus.APPROVED);

        this.applicationRepository.save(application);

        try {
            sendApprovalEmail(application);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return modelMapper.map(application, ApplicationResponse.class);
    }

    @Override
    public ApplicationResponse declineApplication(Long applicationId) {
        Application application = this.applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + applicationId));

        application.setStatus(ApplicationStatus.DENIED);

        this.applicationRepository.save(application);

        try {
            sendDeclineEmail(application);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return this.modelMapper.map(application, ApplicationResponse.class);
    }

    private void sendApprovalEmail(Application application) throws MessagingException {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(application.getUser().getEmail());
        helper.setSubject("Application Approved");

        String text = "Dear " + application.getUser().getFirstName() + ",\n\n"
                + "Your application for " + application.getRequestType() + " has been approved.\n\n"
                + "Details:\n"
                + "Name: " + application.getName() + "\n"
                + "Pick Up Time: " + application.getPickUpTime() + "\n"
                + "Return Time: " + application.getReturnTime() + "\n\n"
                + "Best regards,\nYour Team";

        helper.setText(text);
        this. javaMailSender.send(message);
    }

    private void sendDeclineEmail(Application application) throws MessagingException {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(application.getUser().getEmail());
        helper.setSubject("Application Declined");

        String text = "Dear " + application.getUser().getFirstName() + ",\n\n"
                + "We regret to inform you that your application for " + application.getRequestType() + " has been declined.\n\n"
                + "Best regards,\nYour Team";

        helper.setText(text);
        this.javaMailSender.send(message);
    }

    public void sendCreateApplicationEmail(Application application) {
        String approvalLink = "http://localhost:4200/applications/review/" + application.getId();

        Optional<User> optional = this.userRepository.findById(1L);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(optional.get().getEmail());
        message.setSubject("New Application Created");
        message.setText("New application has been successfully created with status new.\n\n"
                + "Application Details:\n"
                + "Name: " + application.getName() + "\n"
                + "Email: " + application.getEmail() + "\n"
                + "Request Type: " + application.getRequestType() + "\n"
                + "Pick Up Time: " + application.getPickUpTime() + "\n"
                + "Return Time: " + application.getReturnTime() + "\n\n"
                + "You can review and approve/decline the application here: " + approvalLink);

        this.javaMailSender.send(message);
    }

    public void sendUpdateApplicationEmail(Application oldApplication, Application newApplication) {
        String approvalLink = "http://localhost:4200/applications/review/" + newApplication.getId();

        Optional<Application> optional = this.applicationRepository.findById(1L);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(optional.get().getEmail());
        message.setTo(newApplication.getUser().getEmail());
        message.setSubject("Application Updated");
        message.setText("The application has been updated. Below are the changes made:\n\n"
                + "Previous Details:\n"
                + "Name: " + oldApplication.getName() + "\n"
                + "Email: " + oldApplication.getEmail() + "\n"
                + "Pick Up Time: " + oldApplication.getPickUpTime() + "\n"
                + "Return Time: " + oldApplication.getReturnTime() + "\n\n"
                + "Updated Details:\n"
                + "Name: " + newApplication.getName() + "\n"
                + "Email: " + newApplication.getEmail() + "\n"
                + "Pick Up Time: " + newApplication.getPickUpTime() + "\n"
                + "Return Time: " + newApplication.getReturnTime() + "\n\n"
                + "You can review and approve/decline your updated application here: " + approvalLink + "\n");

        this.javaMailSender.send(message);
    }

    private void handleApplicationDetails(Long applicationId, ApplicationDetailsRequest detailsRequest) {
        if (detailsRequest != null) {
            Application application = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid application ID: " + applicationId));

            ApplicationDetails applicationDetails = applicationDetailsRepository.findByApplicationId(application.getId())
                    .orElse(new ApplicationDetails());

            applicationDetails.setFirstName(detailsRequest.getFirstName());
            applicationDetails.setLastName(detailsRequest.getLastName());
            applicationDetails.setEmail(detailsRequest.getEmail());
            applicationDetails.setPhoneNumber(detailsRequest.getPhoneNumber());

            applicationDetails.setAddress(detailsRequest.getAddress());
            applicationDetails.setCity(detailsRequest.getCity());
            applicationDetails.setState(detailsRequest.getState());
            applicationDetails.setPostalCode(detailsRequest.getPostalCode());
            applicationDetails.setCountry(detailsRequest.getCountry());

            applicationDetails.setReasonForAdoption(detailsRequest.getReasonForAdoption());
            applicationDetails.setAnimal(animalRepository.findById(detailsRequest.getAnimalId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid animal ID: " + detailsRequest.getAnimalId())));
            applicationDetails.setPickUpTime(detailsRequest.getPickUpTime());
            applicationDetails.setReturnTime(detailsRequest.getReturnTime());

            applicationDetails.setHasPreviousExperienceWithPets(detailsRequest.getHasPreviousExperienceWithPets());
            applicationDetails.setHasOtherPets(detailsRequest.getHasOtherPets());
            applicationDetails.setHasChildren(detailsRequest.getHasChildren());
            applicationDetails.setHasFencedYard(detailsRequest.getHasFencedYard());
            applicationDetails.setReferenceContact(detailsRequest.getReferenceContact());
            applicationDetails.setBackgroundCheckStatus(detailsRequest.getBackgroundCheckStatus());

            applicationDetails.setApplication(application);

            applicationDetailsRepository.save(applicationDetails);
        }
    }
}