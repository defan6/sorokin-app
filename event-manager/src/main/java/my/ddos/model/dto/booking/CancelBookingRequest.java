package my.ddos.model.dto.booking;

import jakarta.validation.constraints.NotNull;

public record CancelBookingRequest(@NotNull Long bookingId) {
}
