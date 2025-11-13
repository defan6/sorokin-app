package my.ddos.controller.unit;

import my.ddos.controller.rest.EventController;
import my.ddos.model.dto.event.EventRequest;
import my.ddos.model.dto.event.EventResponse;
import my.ddos.model.dto.event.PatchEventRequest;
import my.ddos.service.event.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    void getEvent_shouldReturnEvent_whenEventExists() {
        // Given
        long eventId = 1L;
        EventResponse eventResponse = new EventResponse(eventId, "Test Event", "Description", LocalDateTime.now(), 100L, 1L);
        when(eventService.getEvent(eventId)).thenReturn(eventResponse);

        // When
        ResponseEntity<EventResponse> responseEntity = eventController.getEvent(eventId);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(eventResponse);
    }

    @Test
    void getAllEvents_shouldReturnListOfEvents() {
        // Given
        EventResponse event1 = new EventResponse(1L, "Event 1", "Desc 1", LocalDateTime.now(), 10L, 100L);
        EventResponse event2 = new EventResponse(2L, "Event 2", "Desc 2", LocalDateTime.now(), 20L, 200L);
        List<EventResponse> events = List.of(event1, event2);
        when(eventService.getAllEvents()).thenReturn(events);

        // When
        ResponseEntity<List<EventResponse>> responseEntity = eventController.getAllEvents();

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(events);
    }

    @Test
    void createEvent_shouldCreateEvent() {
        // Given
        String username = "testuser";
        EventRequest eventRequest = new EventRequest("New Event", "Description", LocalDateTime.now(), 1L);
        EventResponse createdEvent = new EventResponse(1L, "New Event", "Description", eventRequest.eventDate(), 100L, 1L);
        when(eventService.createEvent(any(EventRequest.class), eq(username))).thenReturn(createdEvent);

        // When
        ResponseEntity<EventResponse> responseEntity = eventController.createEvent(eventRequest, username);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getLocation()).isEqualTo(URI.create("/api/events/" + createdEvent.id()));
        assertThat(responseEntity.getBody()).isEqualTo(createdEvent);
    }

    @Test
    void patchEvent_shouldUpdateEvent() {
        // Given
        long eventId = 1L;
        String username = "testuser";
        PatchEventRequest patchRequest = new PatchEventRequest(Optional.of("Updated Title"), Optional.empty(), Optional.empty());
        EventResponse updatedEvent = new EventResponse(eventId, "Updated Title", "Description", LocalDateTime.now(), 100L, 1L);
        when(eventService.patchEvent(eq(eventId), any(PatchEventRequest.class), eq(username))).thenReturn(updatedEvent);

        // When
        ResponseEntity<EventResponse> responseEntity = eventController.patchEvent(eventId, patchRequest, username);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(updatedEvent);
    }

    @Test
    void deleteEvent_shouldDeleteEvent() {
        // Given
        long eventId = 1L;
        doNothing().when(eventService).deleteEvent(eventId);

        // When
        ResponseEntity<Void> responseEntity = eventController.deleteEvent(eventId);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
