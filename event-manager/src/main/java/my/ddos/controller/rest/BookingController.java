package my.ddos.controller.rest;


import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.booking.BookingRequest;
import my.ddos.model.dto.booking.RegisterBookingResponse;
import my.ddos.model.dto.booking.UserBookingResponse;
import my.ddos.service.booking.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/my")
    public ResponseEntity<UserBookingResponse> getAllMyBookings(ServerHttpRequest request){
        String username = request.getHeaders().getFirst("X-Username");
        return ResponseEntity.ok(bookingService.getMyBookings(username));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<UserBookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }


    @PostMapping
    public ResponseEntity<RegisterBookingResponse> createBooking(@RequestBody BookingRequest bookingRequest, ServerHttpRequest request) {
        String username = request.getHeaders().getFirst("X-Username");
        return ResponseEntity.ok(bookingService.createBooking(bookingRequest, username));
    }
}
