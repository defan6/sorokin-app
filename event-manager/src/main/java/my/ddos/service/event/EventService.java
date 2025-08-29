package my.ddos.service.event;

import my.ddos.model.dto.event.EventRequest;
import my.ddos.model.dto.event.EventResponse;
import my.ddos.model.dto.event.PatchEventRequest;

import java.util.List;

public interface EventService {
    EventResponse getEvent(Long id);

    List<EventResponse> getAllEvents();

    EventResponse createEvent(EventRequest eventRequest);

    EventResponse patchEvent(Long id, PatchEventRequest patchEventRequest);

    void deleteEvent(Long id);
}
