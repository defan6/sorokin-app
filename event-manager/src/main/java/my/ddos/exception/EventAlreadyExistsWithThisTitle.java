package my.ddos.exception;

public class EventAlreadyExistsWithThisTitle extends RuntimeException {
    public EventAlreadyExistsWithThisTitle(String message) {
        super(message);
    }
}
