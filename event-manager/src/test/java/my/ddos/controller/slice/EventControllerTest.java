package my.ddos.controller.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.ddos.CreateAuthenticationObjectFilter;
import my.ddos.config.security.SecurityConfig;
import my.ddos.controller.rest.EventController;
import my.ddos.model.dto.event.EventRequest;
import my.ddos.model.dto.event.EventResponse;
import my.ddos.model.dto.event.PatchEventRequest;
import my.ddos.service.event.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = EventController.class)
@Import({SecurityConfig.class, CreateAuthenticationObjectFilter.class})
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
        mockMvc.perform(get("/api/manager/events/{id}", eventId)
                        .header("X-User-Id", "1")
                        .header("X-Username", "user")
                        .header("X-User-Roles", "ROLE_USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventId))
                .andExpect(jsonPath("$.title").value("Test Event"));
    }

    @Test
    void getAllEvents_shouldReturnListOfEvents_whenEventsExist() throws Exception {
        // Given
        EventResponse event1 = new EventResponse(1L, "Event 1", "Desc 1", LocalDateTime.now(), 10L, 100L);
        EventResponse event2 = new EventResponse(2L, "Event 2", "Desc 2", LocalDateTime.now(), 20L, 200L);
        List<EventResponse> events = List.of(event1, event2);

        when(eventService.getAllEvents()).thenReturn(events);

        // When & Then
        mockMvc.perform(get("/api/manager/events")
                        .header("X-User-Id", "1")
                        .header("X-Username", "user")
                        .header("X-User-Roles", "ROLE_USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }


    @Test
    void createEvent_asAdmin_shouldCreateEvent() throws Exception {
        // Given
        String username = "admin";
        EventRequest eventRequest = new EventRequest("New Event", "Description", LocalDateTime.now(), 1L);
        EventResponse createdEvent = new EventResponse(1L, "New Event", "Description", eventRequest.eventDate(), 100L, 1L);

        when(eventService.createEvent(any(EventRequest.class), eq(username))).thenReturn(createdEvent);

        // When & Then
        mockMvc.perform(post("/api/manager/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "1")
                        .header("X-Username", username)
                        .header("X-User-Roles", "ROLE_ADMIN")
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/events/" + createdEvent.id()));
    }

    @Test
    void createEvent_asUser_shouldReturnForbidden() throws Exception {
        // Given
        String username = "user";
        EventRequest eventRequest = new EventRequest("New Event", "Description", LocalDateTime.now(), 1L);

        // When & Then
        mockMvc.perform(post("/api/manager/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "2")
                        .header("X-Username", username)
                        .header("X-User-Roles", "ROLE_USER")
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void patchEvent_shouldUpdateEvent() throws Exception {
        // Given
        long eventId = 1L;
        String username = "testuser";
        PatchEventRequest patchRequest = new PatchEventRequest("Updated Title", null, null);
        EventResponse updatedEvent = new EventResponse(eventId, "Updated Title", "Description", LocalDateTime.now(), 100L, 1L);

        when(eventService.patchEvent(eq(eventId), any(PatchEventRequest.class), eq(username))).thenReturn(updatedEvent);

        // When & Then
        mockMvc.perform(patch("/api/manager/events/{id}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "1")
                        .header("X-Username", username)
                        .header("X-User-Roles", "ROLE_ADMIN")
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedEvent.id()));
    }

    @Test
    void deleteEvent_asAdmin_shouldDeleteEvent() throws Exception {
        // Given
        long eventId = 1L;
        doNothing().when(eventService).deleteEvent(eventId);

        // When & Then
        mockMvc.perform(delete("/api/manager/events/{id}", eventId)
                        .header("X-User-Id", "1")
                        .header("X-Username", "admin")
                        .header("X-User-Roles", "ROLE_ADMIN"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEvent_asUser_shouldReturnForbidden() throws Exception {
        // Given
        long eventId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/manager/events/{id}", eventId)
                        .header("X-User-Id", "2")
                        .header("X-Username", "user")
                        .header("X-User-Roles", "ROLE_USER"))
                .andExpect(status().isForbidden());
    }
}
