package com.universityproject.backendproject.model.dto.application.response;

import com.universityproject.backendproject.model.enums.ApplicationStatus;
import com.universityproject.backendproject.model.enums.ApplicationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@Getter
@Setter
public class ApplicationDetailsResponse {
    private Long id; // Corresponds to the ApplicationDetails entity ID
    private Long applicationId; // ID of the associated Application
    private String firstName; // First name from the adoption form
    private String lastName; // Last name from the adoption form
    private String email; // Email from the adoption form
    private String phoneNumber; // Phone number from the adoption form
    private String address; // Address from the adoption form
    private String city; // City from the adoption form
    private String state; // State from the adoption form
    private String postalCode; // Postal code from the adoption form
    private String country; // Country from the adoption form
    private String reasonForAdoption; // Reason for adoption from the adoption form
    private Long animalId; // ID of the animal being adopted
    private String pickUpTime; // Pick-up time from the adoption form
    private String returnTime; // Return time from the adoption form
    private Boolean hasPreviousExperienceWithPets; // Previous experience with pets
    private Boolean hasOtherPets; // If the applicant has other pets
    private Boolean hasChildren; // If the applicant has children
    private Boolean hasFencedYard; // If the applicant has a fenced yard
    private String referenceContact; // Reference contact from the adoption form
    private String backgroundCheckStatus; // Background check status
}
