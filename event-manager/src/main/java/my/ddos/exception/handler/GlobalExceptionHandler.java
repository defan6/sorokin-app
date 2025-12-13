package my.ddos.exception.handler;


import jakarta.servlet.http.HttpServletRequest;
import my.ddos.exception.*;
import my.ddos.model.dto.ExceptionBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

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
    public ResponseEntity<String> handleEventNotFoundException(EventNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }


    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        Map<String, Object> body = Map.of(
                "status", 403,
                "error", "Forbidden",
                "message", "You do not have permission to access this resource",
                "path", request.getRequestURI()
        );
        return body;
    }


    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
