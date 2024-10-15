package com.universityproject.backendproject.service.event;

import com.universityproject.backendproject.exception.event.EventCreationException;
import com.universityproject.backendproject.exception.event.EventNotFoundException;
import com.universityproject.backendproject.exception.event.EventUpdateException;
import com.universityproject.backendproject.model.dto.event.request.EventRequest;
import com.universityproject.backendproject.model.dto.event.response.EventResponse;
import com.universityproject.backendproject.model.entity.Event;
import com.universityproject.backendproject.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private static final Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;

    public List<EventResponse> getCurrentEvents() {
        log.info("Fetching current events.");
        return eventRepository.findByDateTimeAfter(LocalDateTime.now())
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getPastEvents() {
        log.info("Fetching past events.");
        return eventRepository.findByDateTimeBefore(LocalDateTime.now())
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public EventResponse createEvent(EventRequest eventRequest) {
        try {
            log.info("Creating event: {}", eventRequest.getTitle());
            Event event = new Event();
            event.setTitle(eventRequest.getTitle());
            event.setDescription(eventRequest.getDescription());
            event.setDateTime(eventRequest.getDateTime());
            Event savedEvent = eventRepository.save(event);
            log.info("Event created with ID: {}", savedEvent.getId());
            return convertToResponse(savedEvent);
        } catch (Exception e) {
            log.error("Error creating event: {}", e.getMessage(), e);
            throw new EventCreationException("Failed to create event.", e);
        }
    }

    public EventResponse updateEvent(Long id, EventRequest eventRequest) {
        log.info("Updating event with ID: {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + id));

        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setDateTime(eventRequest.getDateTime());

        Event updatedEvent;
        try {
            updatedEvent = eventRepository.save(event);
            log.info("Event updated with ID: {}", updatedEvent.getId());
        } catch (Exception e) {
            log.error("Error updating event: {}", e.getMessage(), e);
            throw new EventUpdateException("Failed to update event.", e);
        }

        return convertToResponse(updatedEvent);
    }

    public void deleteEvent(Long id) {
        log.info("Deleting event with ID: {}", id);
        if (!eventRepository.existsById(id)) {
            log.error("Attempted to delete non-existent event with ID: {}", id);
            throw new EventNotFoundException("Event not found with ID: " + id);
        }
        eventRepository.deleteById(id);
        log.info("Event deleted with ID: {}", id);
    }

    private EventResponse convertToResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setDateTime(event.getDateTime());
        return response;
    }

    public List<EventResponse> getUpcomingEvents() {
        log.info("Fetching upcoming events.");
        return eventRepository.findAll().stream()
                .filter(Event::isUpcoming)
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getCompletedEvents() {
        log.info("Fetching completed events.");
        return eventRepository.findAll().stream()
                .filter(event -> !event.isUpcoming())
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}
