package my.ddos.mapper;

import my.ddos.model.dto.kafka.EventBooking;
import my.ddos.model.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class EventBookingMapper {

    public EventBooking toEventBooking(Booking booking){
        return new EventBooking(
                booking.getEvent().getId(),
                booking.getBookingStatus(),
                booking.getRegisteredAt(),
                booking.getUser().getUsername()
        );
    }
}
