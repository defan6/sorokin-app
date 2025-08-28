package my.ddos.mapper;

import my.ddos.model.dto.event.EventResponse;
import my.ddos.model.entity.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventResponse toResponse(Event entity);
}
