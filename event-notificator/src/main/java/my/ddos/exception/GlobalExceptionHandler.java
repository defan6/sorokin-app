package my.ddos.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler
    public ResponseEntity<String> handleNotificationNotFound(NotificationNotFoundException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
