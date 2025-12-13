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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomProblemDetail handleBookingValidate(BookingValidateException e){
        CustomProblemDetail pb = new CustomProblemDetail();
        pb.setTimestamp(Instant.now());
        pb.setTitle("Booking failed");
        pb.setDetail(String.join(", ", e.getErrors()));
        return pb;
    }


    @ExceptionHandler(VenueValidateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomProblemDetail handleVenueValidate(VenueValidateException e){
        CustomProblemDetail pb = new CustomProblemDetail();
        pb.setTimestamp(Instant.now());
        pb.setTitle("Venue failed");
        pb.setDetail(String.join(", ", e.getErrors()));
        return pb;
    }

    @ExceptionHandler(EventValidateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomProblemDetail handleEventValidate(EventValidateException e){
        CustomProblemDetail pb = new CustomProblemDetail();
        pb.setTimestamp(Instant.now());
        pb.setTitle("Invalid event");
        pb.setDetail(String.join(", ", e.getErrors()));
        return pb;
    }
    @ExceptionHandler(UserAlreadyRegisteredForEventException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomProblemDetail handleUserAlreadyRegisteredForEvent(
            UserAlreadyRegisteredForEventException e
    ){
        CustomProblemDetail pb = new CustomProblemDetail();
        pb.setTimestamp(Instant.now());
        pb.setTitle("Invalid event");
        pb.setDetail(String.join(", ", e.getErrors()));
        return pb;
    }


    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomProblemDetail handleEventNotFoundException(EventNotFoundException e){
        CustomProblemDetail pb = new CustomProblemDetail();
        pb.setTimestamp(Instant.now());
        pb.setDetail(e.getMessage());
        return pb;
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleException(Exception e){
        CustomProblemDetail pb = new CustomProblemDetail();
        pb.setTimestamp(Instant.now());
        pb.setDetail(e.getMessage());
        return pb;
    }
}
