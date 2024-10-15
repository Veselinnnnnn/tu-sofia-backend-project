package com.universityproject.backendproject.repository;

import com.universityproject.backendproject.model.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {
}