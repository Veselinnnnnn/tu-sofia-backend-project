package com.universityproject.backendproject.repository;

import com.universityproject.backendproject.model.entity.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findByAvailabilityTrue();

    Page<Animal> findByAvailabilityTrue(PageRequest pageable);
}
