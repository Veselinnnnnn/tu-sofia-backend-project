package com.universityproject.backendproject.model.dto.user.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserAdvancedInfoRequest {
    private Long userId; // Reference to the User entity
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean hasPreviousExperienceWithPets;
    private boolean hasOtherPets;
    private String householdType; // e.g., House, Apartment, etc.
    private String employmentStatus; // e.g., Employed, Unemployed, Retired, etc.
    private String reasonForAdoption; // Why they want to adopt
    private boolean hasChildren;
    private boolean hasFencedYard;
    private String referenceContact; // A personal reference contact for verification
    private String backgroundCheckStatus; // E.g., Pending, Cleared, etc.
}
