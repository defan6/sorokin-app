package my.ddos.mapper;

import my.ddos.enums.ReadStatus;
import my.ddos.model.dto.EventBooking;
import my.ddos.model.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class BookingEventMapper {
    public Notification toEntity(EventBooking eventBooking){
        Notification notification = new Notification();
        notification.setReadStatus(ReadStatus.UNREAD);
        notification.setBookingStatus(eventBooking.bookingStatus());
        notification.setMessage(eventBooking.message());
        notification.setUsername(eventBooking.username());
        notification.setEventId(eventBooking.eventId());
        return notification;
    }
}
