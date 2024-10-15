package com.universityproject.backendproject.repository;

import com.universityproject.backendproject.model.entity.EmailThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailThreadRepository extends JpaRepository<EmailThread, Long> {
}
