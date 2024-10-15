package com.universityproject.backendproject.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "advanced_user_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdvancedUserDetails extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private boolean hasPreviousExperienceWithPets;

    @Column(nullable = false)
    private boolean hasOtherPets;

    @Column(nullable = false)
    private String householdType; // e.g., House, Apartment, etc.

    @Column(nullable = false)
    private String employmentStatus; // e.g., Employed, Unemployed, Retired, etc.

    @Column(nullable = false)
    private String reasonForAdoption; // Why they want to adopt

    @Column(nullable = false)
    private boolean hasChildren;

    @Column(nullable = false)
    private boolean hasFencedYard;

    @Column(nullable = false)
    private String referenceContact; // A personal reference contact for verification

    @Column(nullable = false)
    private String backgroundCheckStatus; // E.g., Pending, Cleared, etc.
}