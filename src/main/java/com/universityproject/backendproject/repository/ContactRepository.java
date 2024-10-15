package com.universityproject.backendproject.repository;

import com.universityproject.backendproject.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
