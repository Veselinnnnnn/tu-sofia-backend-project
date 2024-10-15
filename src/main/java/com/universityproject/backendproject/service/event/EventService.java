package com.universityproject.backendproject.service.event;

import com.universityproject.backendproject.model.dto.authentication.request.AuthenticationRequest;
import com.universityproject.backendproject.model.dto.authentication.request.RegisterRequest;
import com.universityproject.backendproject.model.dto.authentication.request.ResetPasswordRequest;
import com.universityproject.backendproject.model.dto.authentication.response.AuthenticationResponse;
import com.universityproject.backendproject.model.dto.event.request.EventRequest;
import com.universityproject.backendproject.model.dto.event.response.EventResponse;
import com.universityproject.backendproject.model.entity.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public interface EventService {
    List<EventResponse> getCurrentEvents();
    List<EventResponse> getPastEvents();
    EventResponse createEvent(EventRequest eventRequest);
    EventResponse updateEvent(Long id, EventRequest eventRequest);
    void deleteEvent(Long id);
    List<EventResponse> getUpcomingEvents();
    List<EventResponse> getCompletedEvents();
}
