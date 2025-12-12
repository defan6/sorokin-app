package my.ddos.controller.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.ddos.CreateAuthenticationObjectFilter;
import my.ddos.config.security.SecurityConfig;
import my.ddos.controller.rest.BookingController;
import my.ddos.enums.BookingStatus;
import my.ddos.handlers.CustomAccessDeniedHandler;
import my.ddos.handlers.CustomAuthenticationEntryPoint;
import my.ddos.model.dto.booking.BookingRequest;
import my.ddos.model.dto.booking.CancelBookingRequest;
import my.ddos.model.dto.booking.RegisterBookingResponse;
import my.ddos.model.dto.booking.UserBookingResponse;
import my.ddos.service.booking.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookingController.class)
@Import({SecurityConfig.class, CreateAuthenticationObjectFilter.class, CustomAccessDeniedHandler.class, CustomAuthenticationEntryPoint.class})
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookingService bookingService;

    @Test
    void getAllMyBookings_shouldReturnUserBookings() throws Exception {
        // Given
        String username = "testuser";
        UserBookingResponse userBookingResponse = new UserBookingResponse(username, Collections.emptyList());
        when(bookingService.getMyBookings(username)).thenReturn(userBookingResponse);

        // When & Then
        mockMvc.perform(get("/api/manager/bookings/my")
                        .header("X-User-Id", "1")
                        .header("X-Username", username)
                        .header("X-User-Roles", "ROLE_USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    void getAllBookings_AsAdmin_shouldReturnAllBookings() throws Exception {
        // Given
        when(bookingService.getAllBookings()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/manager/bookings")
                        .header("X-User-Id", "1")
                        .header("X-Username", "admin")
                        .header("X-User-Roles", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void getAllBookings_AsUser_shouldReturnForbidden() throws Exception {
        // Given
        // No need to mock service, as it should be blocked by security

        // When & Then
        mockMvc.perform(get("/api/manager/bookings")
                        .header("X-User-Id", "2")
                        .header("X-Username", "user")
                        .header("X-User-Roles", "ROLE_USER"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createBooking_shouldCreateBooking() throws Exception {
        // Given
        String username = "testuser";
        BookingRequest bookingRequest = new BookingRequest(1L);
        RegisterBookingResponse registerBookingResponse = new RegisterBookingResponse(1L, "Event", username, BookingStatus.REGISTERED, LocalDateTime.now(), "Success");
        when(bookingService.createBooking(any(BookingRequest.class), eq(username))).thenReturn(registerBookingResponse);

        // When & Then
        mockMvc.perform(post("/api/manager/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "1")
                        .header("X-Username", username)
                        .header("X-User-Roles", "ROLE_USER")
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void cancelBooking_shouldCancelBooking() throws Exception {
        // Given
        String username = "testuser";
        CancelBookingRequest cancelBookingRequest = new CancelBookingRequest(1L);
        doNothing().when(bookingService).cancelBooking(eq(username), any(CancelBookingRequest.class));

        // When & Then
        mockMvc.perform(post("/api/manager/bookings/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "1")
                        .header("X-Username", username)
                        .header("X-User-Roles", "ROLE_USER")
                        .content(objectMapper.writeValueAsString(cancelBookingRequest)))
                .andExpect(status().isOk());
    }
}
