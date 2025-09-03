package my.ddos.validator;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import my.ddos.exception.BookingValidateException;
import my.ddos.exception.EventAlreadyExistsWithThisTitle;
import my.ddos.model.dto.event.EventRequest;
import my.ddos.repository.EventRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventValidator {

    private final Validator validator;

    private final EventRepository eventRepository;


    public void validateEventRequest(EventRequest eventRequest){

        var violates = validator.validate(eventRequest);
        List<String> errors = new ArrayList<>(violates.stream().map(ConstraintViolation::getMessage).toList());
        if(!errors.isEmpty()){
            throw new BookingValidateException(errors);
        }
        if(eventRepository.existsByTitle(eventRequest.title())){
            throw new EventAlreadyExistsWithThisTitle("Event with title " + eventRequest.title() + " already exists");
        }
    }
}
