package my.ddos.controller.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.ddos.CreateAuthenticationObjectFilter;
import my.ddos.config.security.SecurityConfig;
import my.ddos.controller.rest.VenueController;
import my.ddos.model.dto.venue.PatchVenueRequest;
import my.ddos.model.dto.venue.VenueRequest;
import my.ddos.model.dto.venue.VenueResponse;
import my.ddos.service.venue.VenueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = VenueController.class)
@Import({SecurityConfig.class, CreateAuthenticationObjectFilter.class})
class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VenueService venueService;

    private static final List<VenueResponse> venuesList = List.of(
            new VenueResponse(1L, "1", "1", 1L),
            new VenueResponse(2L, "2", "2", 2L),
            new VenueResponse(3L, "3", "3", 3L)
    );

    @Test
    void userWithRoleUser_DoGetAll_shouldReturnForbidden() throws Exception {
        // Given
        when(venueService.getAllVenues()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/manager/venues")
                        .header("X-User-Id", "1")
                        .header("X-Username", "user")
                        .header("X-User-Roles", "ROLE_USER"))
                .andExpect(status().isForbidden());
        verify(venueService, never()).getAllVenues();
    }



    @Test
    void userWithRoleAdmin_DoGetAll_shouldReturnVenues() throws Exception {
        // Given
        when(venueService.getAllVenues()).thenReturn(venuesList);

        // When & Then
        mockMvc.perform(get("/api/manager/venues")
                        .header("X-User-Id", "2")
                        .header("X-Username", "admin")
                        .header("X-User-Roles", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3));
    }

    @Test
    void createVenue_AsAdmin_shouldCreateVenue() throws Exception {
        // Given
        VenueRequest venueRequest = new VenueRequest("New Venue", "Address", 100L);
        VenueResponse venueResponse = new VenueResponse(1L, "New Venue", "Address", 100L);
        when(venueService.create(any(VenueRequest.class))).thenReturn(venueResponse);

        // When & Then
        mockMvc.perform(post("/api/manager/venues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venueRequest))
                        .header("X-User-Id", "1")
                        .header("X-Username", "admin")
                        .header("X-User-Roles", "ROLE_ADMIN"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getById_AsAdmin_shouldReturnVenue() throws Exception {
        // Given
        long venueId = 1L;
        VenueResponse venueResponse = new VenueResponse(venueId, "Venue", "Address", 100L);
        when(venueService.getVenue(venueId)).thenReturn(venueResponse);

        // When & Then
        mockMvc.perform(get("/api/manager/venues/{id}", venueId)
                        .header("X-User-Id", "1")
                        .header("X-Username", "admin")
                        .header("X-User-Roles", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(venueId));
    }

    @Test
    void patchVenue_AsAdmin_shouldUpdateVenue() throws Exception {
        // Given
        long venueId = 1L;
        PatchVenueRequest patchRequest = new PatchVenueRequest("Updated Venue", null, null);
        VenueResponse venueResponse = new VenueResponse(venueId, "Updated Venue", "Address", 100L);
        when(venueService.patchVenue(eq(venueId), any(PatchVenueRequest.class))).thenReturn(venueResponse);

        // When & Then
        mockMvc.perform(patch("/api/manager/venues/{id}", venueId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchRequest))
                        .header("X-User-Id", "1")
                        .header("X-Username", "admin")
                        .header("X-User-Roles", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(venueId));
    }

    @Test
    void deleteVenue_AsAdmin_shouldDeleteVenue() throws Exception {
        // Given
        long venueId = 1L;
        doNothing().when(venueService).deleteVenue(venueId);

        // When & Then
        mockMvc.perform(delete("/api/manager/venues/{id}", venueId)
                        .header("X-User-Id", "1")
                        .header("X-Username", "admin")
                        .header("X-User-Roles", "ROLE_ADMIN"))
                .andExpect(status().isNoContent());
    }
}
