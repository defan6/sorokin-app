package my.ddos.service.booking;

import lombok.RequiredArgsConstructor;
import my.ddos.controller.kafka.KafkaBookingProducer;
import my.ddos.enums.BookingStatus;
import my.ddos.exception.BookingNotFoundException;
import my.ddos.exception.EventNotFoundException;
import my.ddos.mapper.BookingMapper;
import my.ddos.mapper.EventBookingMapper;
import my.ddos.model.dto.booking.*;
import my.ddos.model.dto.kafka.EventBooking;
import my.ddos.model.entity.Booking;
import my.ddos.model.entity.Event;
import my.ddos.model.entity.User;
import my.ddos.repository.BookingRepository;
import my.ddos.repository.EventRepository;
import my.ddos.repository.UserRepository;
import my.ddos.validator.BookingValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    @Value("${success.booking.message}")
    private String successBookingMessage;

    @Value("${success.cancel.booking.message}")
    private String successCancelBookingMessage;

    private final BookingRepository bookingRepository;

    private final EventRepository eventRepository;

    private final BookingMapper bookingMapper;

    private final UserRepository userRepository;

    private final EventBookingMapper eventBookingMapper;

    private final KafkaBookingProducer kafkaBookingProducer;

    private final BookingValidator bookingValidator;
    @Override
    public UserBookingResponse getMyBookings(String username) {
        List<Booking> bookings = bookingRepository.findAllByUserUsername(username);
        List<MyBookingResponse> bookingResponses = bookings.stream().map(bookingMapper::toMyBookingResponse).toList();
        return bookingMapper.toUserBookingResponse(username, bookingResponses);
    }

    @Override
    public List<UserBookingResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        Map<String, List<MyBookingResponse>> grouped = bookings.stream()
                .collect(Collectors.groupingBy(
                        booking -> booking.getUser().getUsername(),
                        Collectors.mapping(bookingMapper::toMyBookingResponse, Collectors.toList())
                ));
        return grouped.entrySet().stream()
                .map(entry -> new UserBookingResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    @Transactional
    public RegisterBookingResponse createBooking(BookingRequest bookingRequest, String username) {

        bookingValidator.validateBookingRequest(bookingRequest, username);

        User user = userRepository.findByUsername(username).get();
        Booking booking = new Booking();

        Event event = eventRepository.findById(bookingRequest.eventId())
                .orElseThrow(()->new EventNotFoundException("Event with id " + bookingRequest.eventId() + " not found"));

        booking.setBookingStatus(BookingStatus.REGISTERED);
        booking.setUser(user);
        booking.setEvent(event);

        Booking savedBooking = bookingRepository.save(booking);
        EventBooking eventBooking = eventBookingMapper.toEventBooking(username, successBookingMessage, savedBooking);

        kafkaBookingProducer.sendToBookingTopic(eventBooking);

        return bookingMapper.toRegisterBookingResponse(successBookingMessage, savedBooking);
    }

    @Override
    public void cancelBooking(String username, CancelBookingRequest cancelBookingRequest) {
        Booking booking = bookingRepository
                .findById(cancelBookingRequest.bookingId())
                .orElseThrow(() -> new BookingNotFoundException
                        ("Booking with id " + cancelBookingRequest.bookingId() + " not found"));
        User user = userRepository.findByUsername(username).get();
        if(!booking.getUser().getId().equals(user.getId())){
            throw new BookingNotFoundException("Booking with id " + cancelBookingRequest.bookingId() + " not found");
        }
        booking.setBookingStatus(BookingStatus.CANCELLED);
        EventBooking eventBooking = eventBookingMapper.toEventBooking(username, successCancelBookingMessage, booking);

        kafkaBookingProducer.sendToBookingTopic(eventBooking);
        bookingRepository.save(booking);
    }
}
