package my.ddos.exception.handler;


import my.ddos.exception.*;
import my.ddos.exception.detail.CustomProblemDetail;
import my.ddos.model.dto.ExceptionBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice

public class GlobalExceptionHandler {


    @ExceptionHandler(BookingValidateException.class)
    public CustomProblemDetail handleBookingValidate(BookingValidateException e){
        CustomProblemDetail pb = new CustomProblemDetail();
        pb.setStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Booking failed");
        pb.setDetail(String.join(", ", e.getErrors()));
        pb.setTimestamp(Instant.now());
        return pb;
    }


    @ExceptionHandler(VenueValidateException.class)
    public CustomProblemDetail handleVenueValidate(VenueValidateException e){
        CustomProblemDetail pb = new CustomProblemDetail();
        pb.setStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Venue failed");
        pb.setDetail(String.join(", ", e.getErrors()));
        pb.setTimestamp(Instant.now());
        return pb;
    }

    @ExceptionHandler(EventValidateException.class)
    public CustomProblemDetail handleEventValidate(EventValidateException e){
        CustomProblemDetail pb = new CustomProblemDetail();
        pb.setStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Invalid event");
        pb.setDetail(String.join(", ", e.getErrors()));
        pb.setTimestamp(Instant.now());
        return pb;
    }

    @ExceptionHandler(EventAlreadyExistsWithThisTitle.class)
    public CustomProblemDetail handleExistsEventWithTitle(EventAlreadyExistsWithThisTitle e) {
        CustomProblemDetail pb = new CustomProblemDetail();
        pb.setStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Event exists");
        pb.setDetail(e.getMessage());
        pb.setTimestamp(Instant.now());
        return pb;
    }

    @ExceptionHandler(UserAlreadyRegisteredForEventException.class)
    public CustomProblemDetail handleUserAlreadyRegisteredForEvent(
            UserAlreadyRegisteredForEventException e
    ){
        CustomProblemDetail pb = new CustomProblemDetail();
        pb.setStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Invalid event");
        pb.setDetail(String.join(", ", e.getErrors()));
        pb.setTimestamp(Instant.now());
        return pb;
    }


    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomProblemDetail handleEventNotFoundException(EventNotFoundException e){
        CustomProblemDetail pb = new CustomProblemDetail();
        pb.setStatus(HttpStatus.NOT_FOUND);
        pb.setDetail(e.getMessage());
        pb.setTimestamp(Instant.now());
        return pb;
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception e){
        CustomProblemDetail pb = new CustomProblemDetail();
        pb.setStatus(HttpStatus.BAD_REQUEST);
        pb.setDetail(e.getMessage());
        pb.setTimestamp(Instant.now());
        return pb;
    }
}
