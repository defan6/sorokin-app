package my.ddos.mapper;

import my.ddos.model.dto.venue.VenueResponse;
import my.ddos.model.entity.Venue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    VenueResponse toResponse(Venue entity);
}
