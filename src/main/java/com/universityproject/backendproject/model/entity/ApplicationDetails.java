package com.universityproject.backendproject.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


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

    @ManyToOne
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    private Animal animal;

    @Column(name = "reason_for_adoption", columnDefinition = "TEXT")
    private String reasonForAdoption;

    @Column(name = "pick_up_time")
    private String pickUpTime;

    @Column(name = "return_time")
    private String returnTime;

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
