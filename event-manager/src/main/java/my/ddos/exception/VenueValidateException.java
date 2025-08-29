package my.ddos.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class VenueValidateException extends RuntimeException {
    private final List<String> errors;
}
