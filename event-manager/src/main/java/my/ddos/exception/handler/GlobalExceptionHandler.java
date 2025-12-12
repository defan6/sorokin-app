package my.ddos.exception.handler;


import my.ddos.exception.*;
import my.ddos.model.dto.ExceptionBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class GlobalExceptionHandler {


    @ExceptionHandler(BookingValidateException.class)
    public ProblemDetail handleBookingValidate(BookingValidateException e){
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Booking failed");
        pb.setDetail(String.join(", ", e.getErrors()));
        return pb;
    }


    @ExceptionHandler
    public ProblemDetail handleVenueValidate(VenueValidateException e){
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Venue failed");
        pb.setDetail(String.join(", ", e.getErrors()));
        return pb;
    }

    @ExceptionHandler
    public ProblemDetail handleEventValidate(EventValidateException e){
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Invalid event");
        pb.setDetail(String.join(", ", e.getErrors()));
        return pb;
    }
    @ExceptionHandler
    public ProblemDetail handleUserAlreadyRegisteredForEvent(
            UserAlreadyRegisteredForEventException e
    ){
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setTitle("Invalid event");
        pb.setDetail(String.join(", ", e.getErrors()));
        return pb;
    }


    @ExceptionHandler
    public ProblemDetail handleEventNotFoundException(EventNotFoundException e){
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pb.setDetail(e.getMessage());
        return pb;
    }


    @ExceptionHandler
    public ProblemDetail handleException(Exception e){
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setDetail(e.getMessage());
        return pb;
    }
}
