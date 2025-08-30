package my.ddos.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import my.ddos.exception.BookingValidateException;
import my.ddos.exception.EventNotFoundException;
import my.ddos.exception.UserAlreadyRegisteredForEventException;
import my.ddos.model.dto.booking.BookingRequest;
import my.ddos.model.entity.Event;
import my.ddos.repository.BookingRepository;
import my.ddos.repository.EventRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingValidator {

    private final Validator validator;

    private final BookingRepository bookingRepository;

    private final EventRepository eventRepository;


    public void validateBookingRequest(BookingRequest bookingRequest, String username){
        var violates = validator.validate(bookingRequest);
        List<String> errors = new ArrayList<>(violates.stream().map(ConstraintViolation::getMessage).toList());
        if(!errors.isEmpty()){
            throw new BookingValidateException(errors);
        }


        if(bookingRequest.eventId() != null && !eventRepository.existsById(bookingRequest.eventId())){
            errors.add("Event with id " + bookingRequest.eventId() + " not found");
            throw new EventNotFoundException("Event with id " + bookingRequest.eventId() + " not found");
        }

        if(bookingRequest.eventId() != null && bookingRepository.existsByUserUsernameAndEventId(username, bookingRequest.eventId())){
            Event event = eventRepository.findById(bookingRequest.eventId()).get();
            errors.add("User with username " + username + " already registered for event " + event.getTitle());
            throw new UserAlreadyRegisteredForEventException(errors);
        }
    }
}
