package my.ddos.mapper;

import my.ddos.model.dto.booking.BookingRequest;
import my.ddos.model.dto.booking.BookingResponse;
import my.ddos.model.dto.booking.RegisterBookingResponse;
import my.ddos.model.dto.booking.UserBookingResponse;
import my.ddos.model.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = BookingMapper.class)
public interface BookingMapper {


    @Mapping(target = "eventId", expression = "java(booking.getEvent() != null ? booking.getEvent().getId() : null)")
    BookingResponse toResponse(Booking booking);



    @Mapping(target = "username", source = "username")
    @Mapping(target = "bookingResponses", source = "bookingResponses")
    UserBookingResponse toUserBookingResponse(String username, List<BookingResponse> bookingResponses);


    @Mapping(target = "eventName", expression = "java(booking.getEvent() != null ? booking.getEvent().getTitle() : null)")
    @Mapping(target = "username", expression = "java(booking.getUser() != null ? booking.getUser().getUsername() : null)")
    @Mapping(target = "message", source = "message")
    RegisterBookingResponse toRegisterBookingResponse(String message, Booking booking);
}
