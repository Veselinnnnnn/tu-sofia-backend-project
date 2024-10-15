package com.universityproject.backendproject.model.dto.application.request;

import com.universityproject.backendproject.model.enums.ApplicationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
@Getter
@Setter
public class ApplicationDetailsRequest {
    private Long applicationId; // ID of the application this details refer to

    // New fields corresponding to your adoption form
    private String firstName; // First name of the applicant
    private String lastName; // Last name of the applicant
    private String email; // Email of the applicant
    private String phoneNumber; // Phone number of the applicant

    // Contact Information
    private String address; // Address of the applicant
    private String city; // City of the applicant
    private String state; // State of the applicant
    private String postalCode; // Postal code of the applicant
    private String country; // Country of the applicant

    // Pet Adoption Details
    private String reasonForAdoption; // Reason for adopting
    private Long animalId; // ID of the animal being adopted
    private String pickUpTime; // Pick up time for the animal
    private String returnTime; // Return time for the animal

    // Additional Information
    private Boolean hasPreviousExperienceWithPets; // Experience with pets
    private Boolean hasOtherPets; // Do they have other pets?
    private Boolean hasChildren; // Do they have children?
    private Boolean hasFencedYard; // Do they have a fenced yard?
    private String referenceContact; // Reference contact details
    private String backgroundCheckStatus; // Status of background check
}