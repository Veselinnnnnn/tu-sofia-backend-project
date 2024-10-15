package com.universityproject.backendproject.service.event;

import com.universityproject.backendproject.model.dto.event.request.EventRequest;
import com.universityproject.backendproject.model.dto.event.response.EventResponse;

import java.util.List;

public interface EventService {
    List<EventResponse> getCurrentEvents();

    List<EventResponse> getPastEvents();

    EventResponse createEvent(EventRequest eventRequest);

    EventResponse updateEvent(Long id, EventRequest eventRequest);

    void deleteEvent(Long id);

    List<EventResponse> getUpcomingEvents();

    List<EventResponse> getCompletedEvents();
}
