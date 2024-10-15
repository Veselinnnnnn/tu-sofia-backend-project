package com.universityproject.backendproject.controller;

import com.universityproject.backendproject.model.dto.animal.request.AnimalRequest;
import com.universityproject.backendproject.model.dto.animal.response.AnimalResponse;
import com.universityproject.backendproject.model.dto.animal.response.AnimalWalkResponse;
import com.universityproject.backendproject.model.dto.event.request.EventRequest;
import com.universityproject.backendproject.model.dto.event.response.EventResponse;
import com.universityproject.backendproject.service.animal.AnimalService;
import com.universityproject.backendproject.service.event.EventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/current")
    public ResponseEntity<List<EventResponse>> getCurrentEvents() {
        List<EventResponse> events = eventService.getCurrentEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/past")
    public ResponseEntity<List<EventResponse>> getPastEvents() {
        List<EventResponse> events = eventService.getPastEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest) {
        EventResponse createdEvent = eventService.createEvent(eventRequest);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id, @RequestBody EventRequest eventRequest) {
        EventResponse updatedEvent = eventService.updateEvent(id, eventRequest);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponse>> getUpcomingEvents() {
        List<EventResponse> events = eventService.getUpcomingEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<EventResponse>> getCompletedEvents() {
        List<EventResponse> events = eventService.getCompletedEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}
