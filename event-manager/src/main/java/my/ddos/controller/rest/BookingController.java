package my.ddos.controller.rest;


import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.booking.BookingRequest;
import my.ddos.model.dto.booking.RegisterBookingResponse;
import my.ddos.model.dto.booking.UserBookingResponse;
import my.ddos.service.booking.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/my")
    public ResponseEntity<UserBookingResponse> getAllMyBookings(){
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserBookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }


    @PostMapping
    public ResponseEntity<RegisterBookingResponse> createBooking(@RequestBody BookingRequest bookingRequest) {
        return ResponseEntity.ok(bookingService.createBooking(bookingRequest));
    }
}
