package com.universityproject.backendproject.repository;

import com.universityproject.backendproject.model.dto.application.request.ApplicationRequest;
import com.universityproject.backendproject.model.entity.AdvancedUserDetails;
import com.universityproject.backendproject.model.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByEmail(String email);

    List<Application> findByUserId(Long userId);
}
