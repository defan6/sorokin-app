package my.ddos.mapper;

import my.ddos.model.dto.booking.*;
import my.ddos.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = BookingMapper.class)
public interface BookingMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "eventName", source = "event.title")
    @Mapping(target = "eventDate", source = "event.eventDate")
    @Mapping(target = "venueName", source = "event.venue.name")
    @Mapping(target = "status", source = "bookingStatus")
    MyBookingResponse toMyBookingResponse(Booking booking);

    @Mapping(target = "eventId", expression = "java(booking.getEvent() != null ? booking.getEvent().getId() : null)")
    BookingResponse toResponse(Booking booking);



    @Mapping(target = "username", source = "username")
    @Mapping(target = "myBookingResponses", source = "myBookingResponses")
    UserBookingResponse toUserBookingResponse(String username, List<MyBookingResponse> myBookingResponses);


    @Mapping(target = "eventName", expression = "java(booking.getEvent() != null ? booking.getEvent().getTitle() : null)")
    @Mapping(target = "username", expression = "java(booking.getUser() != null ? booking.getUser().getUsername() : null)")
    @Mapping(target = "message", source = "message")
    RegisterBookingResponse toRegisterBookingResponse(String message, Booking booking);
}
