package com.universityproject.backendproject.repository;

import com.universityproject.backendproject.model.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByEmail(String email);

    List<Application> findByUserId(Long userId);
}
