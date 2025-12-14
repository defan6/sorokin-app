package my.ddos.service.booking;


import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.ddos.controller.kafka.KafkaBookingProducer;
import my.ddos.enums.BookingStatus;
import my.ddos.exception.BookingAlreadyCancelledException;
import my.ddos.exception.BookingNotFoundException;
import my.ddos.mapper.BookingMapper;
import my.ddos.mapper.EventBookingMapper;
import my.ddos.model.dto.booking.*;
import my.ddos.model.dto.event.EventResponse;
import my.ddos.model.dto.kafka.EventBooking;
import my.ddos.model.dto.user.UserResponse;
import my.ddos.model.entity.Booking;
import my.ddos.model.entity.Event;
import my.ddos.model.entity.User;
import my.ddos.repository.BookingRepository;
import my.ddos.service.event.EventService;
import my.ddos.service.i18n.MessageService;
import my.ddos.service.user.UserService;
import my.ddos.validator.BookingValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {


    private final BookingRepository bookingRepository;
    private final EventService eventService;
    private final UserService userService;
    private final EntityManager entityManager;
    private final BookingMapper bookingMapper;
    private final EventBookingMapper eventBookingMapper;
    private final KafkaBookingProducer kafkaBookingProducer;
    private final BookingValidator bookingValidator;
    private final MessageService messageService;

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

        UserResponse userResponse = userService.getInfoAboutCurrentUser(username);
        User user = entityManager.getReference(User.class, userResponse.getId());

        EventResponse eventResponse = eventService.getEvent(bookingRequest.eventId());
        Event event = entityManager.getReference(Event.class, eventResponse.id());

        Booking booking = new Booking();
        booking.setBookingStatus(BookingStatus.REGISTERED);
        booking.setUser(user);
        booking.setEvent(event);

        Booking savedBooking = bookingRepository.save(booking);
        EventBooking eventBooking = eventBookingMapper.toEventBooking(savedBooking);

        kafkaBookingProducer.sendToBookingTopic(eventBooking);

        return bookingMapper.toRegisterBookingResponse(messageService.getMessage("user.register.success.on.event", null), savedBooking);
    }

    @Override
    public CancelBookingResponse cancelBooking(String username, CancelBookingRequest cancelBookingRequest) {
        Booking booking = bookingRepository
                .findById(cancelBookingRequest.bookingId())
                .orElseThrow(() -> new BookingNotFoundException
                        (messageService.getMessage("user.booking.not.found", new Object[]{cancelBookingRequest.bookingId()})));


        if(booking.getBookingStatus().equals(BookingStatus.CANCELLED)) {
            throw new BookingAlreadyCancelledException(messageService.getMessage("user.cancel.booking.already.cancelled",
                    new Object[]{booking.getEvent().getTitle()}));
        }

        UserResponse userResponse = userService.getInfoAboutCurrentUser(username);
        if (!booking.getUser().getId().equals(userResponse.getId())) {
            throw new BookingNotFoundException
                    (messageService.getMessage("user.booking.not.found", new Object[]{cancelBookingRequest.bookingId()}));
        }
        booking.setBookingStatus(BookingStatus.CANCELLED);
        EventBooking eventBooking = eventBookingMapper.toEventBooking(booking);

        kafkaBookingProducer.sendToBookingTopic(eventBooking);
        bookingRepository.save(booking);
        return new CancelBookingResponse(messageService.getMessage("user.cancel.booking.success", null));
    }


}
