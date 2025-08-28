package my.ddos.mapper;

import my.ddos.model.dto.booking.BookingResponse;
import my.ddos.model.entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, EventMapper.class})
public interface BookingMapper {

    BookingResponse toResponse(Booking entity);
}
