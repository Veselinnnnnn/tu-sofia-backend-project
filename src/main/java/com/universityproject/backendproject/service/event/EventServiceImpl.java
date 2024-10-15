package com.universityproject.backendproject.service.event;

import com.universityproject.backendproject.model.dto.application.response.ApplicationDetailsResponse;
import com.universityproject.backendproject.model.dto.event.request.EventRequest;
import com.universityproject.backendproject.model.dto.event.response.EventResponse;
import com.universityproject.backendproject.model.entity.ApplicationDetails;
import com.universityproject.backendproject.model.entity.Event;
import com.universityproject.backendproject.repository.ApplicationDetailsRepository;
import com.universityproject.backendproject.repository.EventRepository;
import com.universityproject.backendproject.service.applicationDetails.ApplicationDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public List<EventResponse> getCurrentEvents() {
        return eventRepository.findByDateTimeAfter(LocalDateTime.now())
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getPastEvents() {
        return eventRepository.findByDateTimeBefore(LocalDateTime.now())
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public EventResponse createEvent(EventRequest eventRequest) {
        Event event = new Event();
        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setDateTime(eventRequest.getDateTime());
        Event savedEvent = eventRepository.save(event);
        return convertToResponse(savedEvent);
    }

    public EventResponse updateEvent(Long id, EventRequest eventRequest) {
        Event event = eventRepository.findById(id).orElseThrow();
        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setDateTime(eventRequest.getDateTime());
        Event updatedEvent = eventRepository.save(event);
        return convertToResponse(updatedEvent);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
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
        return eventRepository.findAll().stream()
                .filter(Event::isUpcoming)
                .map(this::convertToResponse) // Assuming you have a method for this
                .collect(Collectors.toList());
    }

    public List<EventResponse> getCompletedEvents() {
        return eventRepository.findAll().stream()
                .filter(event -> !event.isUpcoming())
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}

