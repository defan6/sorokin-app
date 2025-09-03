package my.ddos.controller.rest;


import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.booking.BookingRequest;
import my.ddos.model.dto.booking.CancelBookingRequest;
import my.ddos.model.dto.booking.RegisterBookingResponse;
import my.ddos.model.dto.booking.UserBookingResponse;
import my.ddos.service.booking.BookingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Value("${success.cancel.booking.message}")
    private String successCancelBookingMessage;

    @GetMapping("/my")
    public ResponseEntity<UserBookingResponse> getAllMyBookings(@RequestHeader("X-Username") String username){
        return ResponseEntity.ok(bookingService.getMyBookings(username));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<UserBookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }


    @PostMapping
    public ResponseEntity<RegisterBookingResponse> createBooking(@RequestBody BookingRequest bookingRequest,
                                                                 @RequestHeader("X-Username") String username) {
        return ResponseEntity.ok(bookingService.createBooking(bookingRequest, username));
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelBooking(@RequestBody CancelBookingRequest cancelBookingRequest,
                                                @RequestHeader("X-Username") String username) {
        bookingService.cancelBooking(username, cancelBookingRequest);
        return ResponseEntity.ok(successCancelBookingMessage);
    }
}
