package com.universityproject.backendproject.repository;

import com.universityproject.backendproject.model.entity.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AnimalRepository extends JpaRepository<Animal, Long> {

    @Query("SELECT a FROM Animal a WHERE a.availability = false")
    Page<Animal> findAllAvailable(PageRequest pageable);
}
