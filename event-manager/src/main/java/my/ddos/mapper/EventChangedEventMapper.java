package my.ddos.mapper;

import my.ddos.model.dto.kafka.EventChangedEvent;
import my.ddos.model.entity.Event;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventChangedEventMapper {

    public EventChangedEvent toEventChanged(Event event, String changedBy) {
        return new EventChangedEvent(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventDate(),
                LocalDateTime.now(),
                changedBy
        );
    }
}
