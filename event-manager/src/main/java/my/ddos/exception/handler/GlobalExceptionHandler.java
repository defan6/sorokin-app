package my.ddos.exception.handler;


import my.ddos.exception.BookingValidateException;
import my.ddos.exception.EventValidateException;
import my.ddos.exception.UserAlreadyRegisteredForEventException;
import my.ddos.exception.VenueValidateException;
import my.ddos.model.dto.ExceptionBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class GlobalExceptionHandler {


    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleBookingValidate(BookingValidateException e){
        return ResponseEntity.badRequest().body(new ExceptionBody("Booking failed", e.getErrors()));
    }


    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleVenueValidate(VenueValidateException e){
        return ResponseEntity.badRequest().body(new ExceptionBody("Invalid venue", e.getErrors()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleEventValidate(EventValidateException e){
        return ResponseEntity.badRequest().body(new ExceptionBody("Invalid event", e.getErrors()));
    }
    @ExceptionHandler
    public ResponseEntity<ExceptionBody> handleUserAlreadyRegisteredForEvent(
            UserAlreadyRegisteredForEventException e
    ){
        return ResponseEntity.badRequest().body(new ExceptionBody("Invalid event", e.getErrors()));
    }


    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
