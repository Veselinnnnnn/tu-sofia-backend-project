package com.universityproject.backendproject.model.dto.user.response;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserAdvancedInfoResponse {
    private Long userId;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean hasPreviousExperienceWithPets;
    private boolean hasOtherPets;
    private String householdType;
    private String employmentStatus;
    private String reasonForAdoption;
    private boolean hasChildren;
    private boolean hasFencedYard;
    private String referenceContact;
    private String backgroundCheckStatus;
}
