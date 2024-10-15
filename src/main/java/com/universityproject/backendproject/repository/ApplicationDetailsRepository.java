package com.universityproject.backendproject.repository;

import com.universityproject.backendproject.model.entity.ApplicationDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ApplicationDetailsRepository extends JpaRepository<ApplicationDetails, Long> {
    Optional<ApplicationDetails> findByApplicationId(Long applicationId);
}
