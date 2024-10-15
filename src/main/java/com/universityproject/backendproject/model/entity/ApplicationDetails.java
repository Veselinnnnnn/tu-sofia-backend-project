package com.universityproject.backendproject.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Entity
@Table(name = "application_details")
public class ApplicationDetails extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private Application application;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    // Contact Information
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "country", nullable = false)
    private String country;

    // Pet Adoption Details
    @ManyToOne
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    private Animal animal; // Assuming you have an Animal entity

    @Column(name = "reason_for_adoption", columnDefinition = "TEXT")
    private String reasonForAdoption;

    @Column(name = "pick_up_time")
    private String pickUpTime; // Pick up time for the animal

    @Column(name = "return_time")
    private String returnTime; // Return time for the animal

    // Additional Information
    @Column(name = "has_previous_experience_with_pets")
    private Boolean hasPreviousExperienceWithPets;

    @Column(name = "has_other_pets")
    private Boolean hasOtherPets;

    @Column(name = "has_children")
    private Boolean hasChildren;

    @Column(name = "has_fenced_yard")
    private Boolean hasFencedYard;

    @Column(name = "reference_contact", columnDefinition = "TEXT")
    private String referenceContact;

    @Column(name = "background_check_status")
    private String backgroundCheckStatus;
}
