package com.universityproject.backendproject.repository;

import com.universityproject.backendproject.model.entity.AdvancedUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AdvancedUserDetailsRepository extends JpaRepository<AdvancedUserDetails, Long> {
    Optional<AdvancedUserDetails> findByUserId(Long id);
}
