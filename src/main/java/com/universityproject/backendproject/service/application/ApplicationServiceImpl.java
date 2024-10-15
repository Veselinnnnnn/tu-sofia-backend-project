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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);
    private final ApplicationDetailsRepository applicationDetailsRepository;
    private final ApplicationRepository applicationRepository;
    private final AnimalRepository animalRepository;
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public ApplicationResponse create(ApplicationCompositeRequest  request) throws MessagingException {
        // Manually map user and animal fields
        Application application = new Application();
        application.setName(request.getApplicationRequest().getName());
        application.setEmail(request.getApplicationRequest().getEmail());
        application.setRequestDate(LocalDateTime.now());
        application.setStatus(ApplicationStatus.PENDING);
        application.setRequestType(request.getApplicationRequest().getRequestType());
        application.setPickUpTime(request.getApplicationRequest().getPickUpTime());
        application.setReturnTime(request.getApplicationRequest().getReturnTime());
        application.setDate(request.getApplicationRequest().getDate());

        // Set User entity using userId from request
        User user = userRepository.findById(request.getApplicationRequest().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        application.setUser(user);

        // Set Animal entity using animalId from request
        Animal animal = animalRepository.findById(request.getApplicationRequest().getAnimalId())
                .orElseThrow(() -> new IllegalArgumentException("Animal not found"));
        application.setAnimal(animal);

        Application savedRequest = this.applicationRepository.save(application);

        // Handle ApplicationDetails
        handleApplicationDetails(savedRequest.getId(), request.getApplicationDetailsRequest());

        sendCreateApplicationEmail(savedRequest);

        return this.modelMapper.map(savedRequest, ApplicationResponse.class);
    }

    @Override
    public ApplicationResponse findById(Long applicationId) {
        Application application = this.applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + applicationId));

        return modelMapper.map(application, ApplicationResponse.class);
    }

    @Override
    public void updateStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<Application> applications = applicationRepository.findAll();
        for (Application app : applications) {
            if (app.getReturnTime().isBefore(LocalTime.from(now))) {
                app.setStatus(ApplicationStatus.OUTDATED);
                applicationRepository.save(app);
            }
        }
    }

    @Override
    public void delete(Long applicationId) {
        Optional<Application> application = this.applicationRepository.findById(applicationId);
        application.ifPresent(this.applicationRepository::delete);
    }

    @Override
    public void update(Long applicationId, ApplicationCompositeRequest request) throws MessagingException {
        // Retrieve the existing application
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        // Update application fields
        application.setName(request.getApplicationRequest().getName());
        application.setEmail(request.getApplicationRequest().getEmail());
        application.setRequestType(request.getApplicationRequest().getRequestType());
        application.setPickUpTime(request.getApplicationRequest().getPickUpTime());
        application.setReturnTime(request.getApplicationRequest().getReturnTime());
        application.setDate(request.getApplicationRequest().getDate());

        // Update user and animal entities
        User user = userRepository.findById(request.getApplicationRequest().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        application.setUser(user);

        Animal animal = animalRepository.findById(request.getApplicationRequest().getAnimalId())
                .orElseThrow(() -> new IllegalArgumentException("Animal not found"));
        application.setAnimal(animal);

        // Save updated application
        applicationRepository.save(application);

        // Handle ApplicationDetails update
        handleApplicationDetails(application.getId(), request.getApplicationDetailsRequest());
    }
//    @Override
//    public ApplicationResponse update(Long applicationId, ApplicationRequest request) throws MessagingException {
//        Application existingApplication = applicationRepository.findById(applicationId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID: " + applicationId));
//
//        Application oldApplication = new Application();
//        modelMapper.map(existingApplication, oldApplication);
//
//        // Map fields from ApplicationRequest to existingApplication
//        mapApplicationFields(existingApplication, request);
//
//        // Save the updated application
//        Application updatedApplication = applicationRepository.save(existingApplication);
//
//        // Send email notification to admin
//        sendUpdateApplicationEmail(oldApplication, updatedApplication);
//
//        // Map the updated application to ApplicationResponse
//        return modelMapper.map(updatedApplication, ApplicationResponse.class);
//    }

    @Override
    public List<ApplicationResponse> getApplicationsByUserId(Long userId) {
        List<Application> applications = this.applicationRepository.findByUserId(userId);

        return applications.stream()
                .map(application -> this.modelMapper.map(application, ApplicationResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationResponse> getApplicationByEmail(String email) {
        List<Application> requests = this.applicationRepository.findByEmail(email);

        return requests.stream()
                .map(request -> this.modelMapper.map(request, ApplicationResponse.class))
                .collect(Collectors.toList());
    }

    private void sendEmailToAdmin(Application request) throws MessagingException {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo("admin@animalshelter.com");
        helper.setSubject("New " + request.getRequestType() + " Request");
        helper.setText("A new request of type " + request.getRequestType() +
                " has been submitted. Request ID: " + request.getId());

        this.javaMailSender.send(message);
    }

    @Override
    public ApplicationResponse approveApplication(Long applicationId) throws MessagingException {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + applicationId));

        // Change the status to APPROVED
        application.setStatus(ApplicationStatus.APPROVED);

        // Save the updated application
        applicationRepository.save(application);

        // Send notification email to admin and user
        sendApprovalEmail(application);

        return modelMapper.map(application, ApplicationResponse.class);
    }

    @Override
    public ApplicationResponse declineApplication(Long applicationId) throws MessagingException {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + applicationId));

        // Change the status to DECLINED
        application.setStatus(ApplicationStatus.DENIED);

        // Save the updated application
        applicationRepository.save(application);

        // Send notification email to admin and user
        sendDeclineEmail(application);

        return modelMapper.map(application, ApplicationResponse.class);
    }

    private void sendApprovalEmail(Application application) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
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
        javaMailSender.send(message);
    }

    private void sendDeclineEmail(Application application) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(application.getUser().getEmail());
        helper.setSubject("Application Declined");

        String text = "Dear " + application.getUser().getFirstName() + ",\n\n"
                + "We regret to inform you that your application for " + application.getRequestType() + " has been declined.\n\n"
                + "Best regards,\nYour Team";

        helper.setText(text);
        javaMailSender.send(message);
    }

    private void mapApplicationFields(Application application, ApplicationRequest request) {
        application.setName(request.getName());
        application.setEmail(request.getEmail());
        application.setRequestType(request.getRequestType());
        application.setPickUpTime(request.getPickUpTime());
        application.setReturnTime(request.getReturnTime());
        application.setDate(request.getDate());

        // Fetch and set the User entity
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + request.getUserId()));
        application.setUser(user);

        // Fetch and set the Animal entity
        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid animal ID: " + request.getAnimalId()));
        application.setAnimal(animal);
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

        javaMailSender.send(message);
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

        javaMailSender.send(message);
    }

    private void handleApplicationDetails(Long applicationId, ApplicationDetailsRequest detailsRequest) {
        System.out.println("KUREC");
        System.out.println(detailsRequest);

        if (detailsRequest != null) {
            // Retrieve the application
            Application application = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid application ID: " + applicationId));

            // Check if ApplicationDetails already exists
            ApplicationDetails applicationDetails = applicationDetailsRepository.findByApplicationId(application.getId())
                    .orElse(new ApplicationDetails());

            // Set applicant information
            applicationDetails.setFirstName(detailsRequest.getFirstName());
            applicationDetails.setLastName(detailsRequest.getLastName());
            applicationDetails.setEmail(detailsRequest.getEmail());
            applicationDetails.setPhoneNumber(detailsRequest.getPhoneNumber());

            // Set contact information
            applicationDetails.setAddress(detailsRequest.getAddress());
            applicationDetails.setCity(detailsRequest.getCity());
            applicationDetails.setState(detailsRequest.getState());
            applicationDetails.setPostalCode(detailsRequest.getPostalCode());
            applicationDetails.setCountry(detailsRequest.getCountry());

            // Set pet adoption details
            applicationDetails.setReasonForAdoption(detailsRequest.getReasonForAdoption());
            applicationDetails.setAnimal(animalRepository.findById(detailsRequest.getAnimalId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid animal ID: " + detailsRequest.getAnimalId())));
            applicationDetails.setPickUpTime(detailsRequest.getPickUpTime());
            applicationDetails.setReturnTime(detailsRequest.getReturnTime());

            // Set additional information
            applicationDetails.setHasPreviousExperienceWithPets(detailsRequest.getHasPreviousExperienceWithPets());
            applicationDetails.setHasOtherPets(detailsRequest.getHasOtherPets());
            applicationDetails.setHasChildren(detailsRequest.getHasChildren());
            applicationDetails.setHasFencedYard(detailsRequest.getHasFencedYard());
            applicationDetails.setReferenceContact(detailsRequest.getReferenceContact());
            applicationDetails.setBackgroundCheckStatus(detailsRequest.getBackgroundCheckStatus());

            // Set the application reference in applicationDetails
            applicationDetails.setApplication(application);

            // Save the application details
            applicationDetailsRepository.save(applicationDetails);
        }
    }

}


