package my.ddos.mapper;

import my.ddos.model.dto.venue.PatchVenueRequest;
import my.ddos.model.dto.venue.VenueRequest;
import my.ddos.model.dto.venue.VenueResponse;
import my.ddos.model.entity.Venue;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    VenueResponse toResponse(Venue entity);

    Venue toEntity(VenueRequest venueRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            ignoreByDefault = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "capacity", source = "capacity")
    void patchFromRequest(PatchVenueRequest patchVenueRequest, @MappingTarget Venue venue);
}
