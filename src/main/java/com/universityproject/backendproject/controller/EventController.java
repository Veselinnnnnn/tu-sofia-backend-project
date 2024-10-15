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
    public List<EventResponse> getCurrentEvents() {
        return eventService.getCurrentEvents();
    }

    @GetMapping("/past")
    public List<EventResponse> getPastEvents() {
        return eventService.getPastEvents();
    }

    @PostMapping
    public EventResponse createEvent(@RequestBody EventRequest eventRequest) {
        return eventService.createEvent(eventRequest);
    }

    @PutMapping
    public EventResponse updateEvent(@RequestParam Long id, @RequestBody EventRequest eventRequest) {
        return eventService.updateEvent(id, eventRequest);
    }

    @DeleteMapping
    public void deleteEvent(@RequestParam Long id) {
        eventService.deleteEvent(id);
    }

    @GetMapping("/upcoming")
    public List<EventResponse> getUpcomingEvents() {
        return eventService.getUpcomingEvents();
    }

    @GetMapping("/completed")
    public List<EventResponse> getCompletedEvents() {
        return eventService.getCompletedEvents();
    }
}
