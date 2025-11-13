package my.ddos.controller.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.ddos.controller.rest.EventController;
import my.ddos.model.dto.event.EventRequest;
import my.ddos.model.dto.event.EventResponse;
import my.ddos.model.dto.event.PatchEventRequest;
import my.ddos.service.event.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EventService eventService;

    @Test
    void getEvent_shouldReturnEvent_whenEventExists() throws Exception {
        // Given
        long eventId = 1L;
        EventResponse eventResponse = new EventResponse(eventId, "Test Event", "Description", LocalDateTime.now(), 100L, 1L);

        when(eventService.getEvent(eventId)).thenReturn(eventResponse);

        // When & Then
        mockMvc.perform(get("/api/manager/events/{id}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId))
                .andExpect(jsonPath("$.title").value("Test Event"));
    }

    @Test
    void getEvent_shouldReturnNotFound_whenEventDoesNotExist() throws Exception {
        // Given
        long eventId = 1L;
        when(eventService.getEvent(eventId)).thenThrow(new my.ddos.exception.EventNotFoundException("Event with id: %d not found".formatted(eventId)));

        // When & Then
        mockMvc.perform(get("/api/manager/events/{id}", eventId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllEvents_shouldReturnListOfEvents_whenEventsExist() throws Exception {
        // Given
        EventResponse event1 = new EventResponse(1L, "Event 1", "Desc 1", LocalDateTime.now(), 10L, 100L);
        EventResponse event2 = new EventResponse(2L, "Event 2", "Desc 2", LocalDateTime.now(), 20L, 200L);
        List<EventResponse> events = List.of(event1, event2);

        when(eventService.getAllEvents()).thenReturn(events);

        // When & Then
        mockMvc.perform(get("/api/manager/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void getAllEvents_shouldReturnEmptyList_whenNoEventsExist() throws Exception {
        // Given
        when(eventService.getAllEvents()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/manager/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void createEvent_shouldCreateEvent_whenRequestIsValid() throws Exception {
        // Given
        String username = "testuser";
        EventRequest eventRequest = new EventRequest("New Event", "Description", LocalDateTime.now(), 1L);
        EventResponse createdEvent = new EventResponse(1L, "New Event", "Description", eventRequest.eventDate(), 100L, 1L);

        when(eventService.createEvent(any(EventRequest.class), eq(username))).thenReturn(createdEvent);

        // When & Then
        mockMvc.perform(post("/api/manager/events/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Username", username)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/events/" + createdEvent.id()))
                .andExpect(jsonPath("$.id").value(createdEvent.id()))
                .andExpect(jsonPath("$.title").value(createdEvent.title()));
    }

    @Test
    void patchEvent_shouldUpdateEvent_whenRequestIsValid() throws Exception {
        // Given
        long eventId = 1L;
        String username = "testuser";
        PatchEventRequest patchRequest = new PatchEventRequest(Optional.of("Updated Title"), Optional.empty(), Optional.empty());
        EventResponse updatedEvent = new EventResponse(eventId, "Updated Title", "Description", LocalDateTime.now(), 100L, 1L);

        when(eventService.patchEvent(eq(eventId), any(PatchEventRequest.class), eq(username))).thenReturn(updatedEvent);

        // When & Then
        mockMvc.perform(patch("/api/manager/events/admin/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Username", username)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedEvent.id()))
                .andExpect(jsonPath("$.title").value(updatedEvent.title()));
    }

    @Test
    void deleteEvent_shouldDeleteEvent_whenEventExists() throws Exception {
        // Given
        long eventId = 1L;
        doNothing().when(eventService).deleteEvent(eventId);

        // When & Then
        mockMvc.perform(delete("/api/manager/events/admin/{id}", eventId))
                .andExpect(status().isNoContent());
    }
}
