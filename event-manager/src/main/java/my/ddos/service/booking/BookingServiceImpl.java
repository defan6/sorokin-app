package my.ddos.service.booking;

import lombok.RequiredArgsConstructor;
import my.ddos.enums.BookingStatus;
import my.ddos.exception.EventNotFoundException;
import my.ddos.mapper.BookingMapper;
import my.ddos.model.dto.booking.BookingRequest;
import my.ddos.model.dto.booking.BookingResponse;
import my.ddos.model.dto.booking.RegisterBookingResponse;
import my.ddos.model.dto.booking.UserBookingResponse;
import my.ddos.model.entity.Booking;
import my.ddos.model.entity.Event;
import my.ddos.model.entity.User;
import my.ddos.repository.BookingRepository;
import my.ddos.repository.EventRepository;
import my.ddos.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;

    private final EventRepository eventRepository;

    private final BookingMapper bookingMapper;

    private final UserRepository userRepository;
    @Override
    public UserBookingResponse getMyBookings() {
        String username = getCurrentUserUsername();
        List<Booking> bookings = bookingRepository.findAllByUserUsername(username);
        List<BookingResponse> bookingResponses = bookings.stream().map(bookingMapper::toResponse).toList();
        return bookingMapper.toUserBookingResponse(username, bookingResponses);
    }

    @Override
    public List<UserBookingResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        Map<String, List<BookingResponse>> grouped = bookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getUser().getUsername(),
                        Collectors.mapping(bookingMapper::toResponse, Collectors.toList())
                ));
        return grouped.entrySet().stream()
                .map(entry -> new UserBookingResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    public RegisterBookingResponse createBooking(BookingRequest bookingRequest) {
        String username = getCurrentUserUsername();
        User user = userRepository.findByUsername(username).orElseThrow();
        Booking booking = new Booking();
        Event event = eventRepository.findById(bookingRequest.eventId())
                .orElseThrow(()->new EventNotFoundException("Event with id " + bookingRequest.eventId() + " not found"));
        booking.setBookingStatus(BookingStatus.REGISTERED);
        booking.setUser(user);
        booking.setEvent(event);

        return bookingMapper.toRegisterBookingResponse(bookingRepository.save(booking));
    }


    private static String getCurrentUserUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return username;
    }
}
