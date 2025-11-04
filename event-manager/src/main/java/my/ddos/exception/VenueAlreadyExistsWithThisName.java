package my.ddos.exception;

public class VenueAlreadyExistsWithThisName extends RuntimeException {
    public VenueAlreadyExistsWithThisName(String message) {
        super(message);
    }
}
