package my.ddos.mapper;

import my.ddos.model.dto.venue.VenueRequest;
import my.ddos.model.dto.venue.VenueResponse;
import my.ddos.model.entity.Venue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    VenueResponse toResponse(Venue entity);

    Venue toEntity(VenueRequest venueRequest);
}
