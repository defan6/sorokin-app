package my.ddos.controller.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.ddos.controller.rest.VenueController;
import my.ddos.model.dto.venue.PatchVenueRequest;
import my.ddos.model.dto.venue.VenueRequest;
import my.ddos.model.dto.venue.VenueResponse;
import my.ddos.service.venue.VenueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(VenueController.class)
class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VenueService venueService;

    @Test
    void getAll_shouldReturnAllVenues() throws Exception {
        // Given
        when(venueService.getAllVenues()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/manager/venues/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void createVenue_shouldCreateVenue() throws Exception {
        // Given
        VenueRequest venueRequest = new VenueRequest("New Venue", "Address", 100L);
        VenueResponse venueResponse = new VenueResponse(1L, "New Venue", "Address", 100L);
        when(venueService.create(any(VenueRequest.class))).thenReturn(venueResponse);

        // When & Then
        mockMvc.perform(post("/api/manager/venues/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getById_shouldReturnVenue() throws Exception {
        // Given
        long venueId = 1L;
        VenueResponse venueResponse = new VenueResponse(venueId, "Venue", "Address", 100L);
        when(venueService.getVenue(venueId)).thenReturn(venueResponse);

        // When & Then
        mockMvc.perform(get("/api/manager/venues/admin/{id}", venueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(venueId));
    }

    @Test
    void patchVenue_shouldUpdateVenue() throws Exception {
        // Given
        long venueId = 1L;
        PatchVenueRequest patchRequest = new PatchVenueRequest(Optional.of("Updated Venue"), Optional.empty(), Optional.empty());
        VenueResponse venueResponse = new VenueResponse(venueId, "Updated Venue", "Address", 100L);
        when(venueService.patchVenue(eq(venueId), any(PatchVenueRequest.class))).thenReturn(venueResponse);

        // When & Then
        mockMvc.perform(patch("/api/manager/venues/admin/{id}", venueId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(venueId));
    }

    @Test
    void deleteVenue_shouldDeleteVenue() throws Exception {
        // Given
        long venueId = 1L;
        doNothing().when(venueService).deleteVenue(venueId);

        // When & Then
        mockMvc.perform(delete("/api/manager/venues/admin/{id}", venueId))
                .andExpect(status().isNoContent());
    }
}
