package com.universityproject.backendproject.repository;

import com.universityproject.backendproject.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDateTimeAfter(LocalDateTime dateTime);
    List<Event> findByDateTimeBefore(LocalDateTime dateTime);
}
