package my.ddos.mapper;

import my.ddos.model.dto.event.EventRequest;
import my.ddos.model.dto.event.EventResponse;
import my.ddos.model.dto.event.PatchEventRequest;
import my.ddos.model.dto.venue.PatchVenueRequest;
import my.ddos.model.entity.Event;
import my.ddos.model.entity.Venue;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BookingMapper.class, VenueMapper.class})
public interface EventMapper {
    @Mapping(target = "venueId", expression = "java(event.getVenue() != null ? event.getVenue().getId() : null)")
    @Mapping(target = "organizerId", expression = "java(event.getOrganizer() != null ? event.getOrganizer().getId() : null)")
    EventResponse toResponse(Event event);

    Event toEntity(EventRequest eventRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchFromRequest(PatchEventRequest patchEventRequest, @MappingTarget Event event);
}
