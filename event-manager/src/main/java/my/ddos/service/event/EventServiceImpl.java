package my.ddos.service.event;

import lombok.RequiredArgsConstructor;
import my.ddos.controller.kafka.KafkaChangeEventProducer;
import my.ddos.exception.EventNotFoundException;
import my.ddos.exception.VenueNotFoundException;
import my.ddos.mapper.EventChangedEventMapper;
import my.ddos.mapper.EventMapper;
import my.ddos.model.dto.event.EventRequest;
import my.ddos.model.dto.event.EventResponse;
import my.ddos.model.dto.event.PatchEventRequest;
import my.ddos.model.dto.kafka.EventChangedEvent;
import my.ddos.model.entity.Event;
import my.ddos.model.entity.User;
import my.ddos.model.entity.Venue;
import my.ddos.repository.EventRepository;
import my.ddos.repository.UserRepository;
import my.ddos.repository.VenueRepository;
import my.ddos.validator.EventValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final VenueRepository venueRepository;

    private  final UserRepository userRepository;

    private final KafkaChangeEventProducer kafkaChangeEventProducer;

    private final EventValidator eventValidator;


    private final EventChangedEventMapper eventChangedEventMapper;
    @Override
    public EventResponse getEvent(Long id) {
        return eventRepository.findById(id).map(eventMapper::toResponse)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + id + " not found"));
    }

    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream().map(eventMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public EventResponse createEvent(EventRequest eventRequest, String username) {
        Event event = eventMapper.toEntity(eventRequest);
        User organizer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User with username " + username + " not found"));
        event.setOrganizer(organizer);
        Venue venue = venueRepository.findById(eventRequest.venueId())
                .orElseThrow(() -> new VenueNotFoundException("Venue with id " + eventRequest.venueId() + " not found"));
        event.setVenue(venue);
        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventResponse patchEvent(Long id, PatchEventRequest patchEventRequest, String changedBy) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + id + " not found."));
        patchEventRequest.title().ifPresent(event::setTitle);
        patchEventRequest.eventDate().ifPresent(event::setEventDate);
        patchEventRequest.description().ifPresent(event::setDescription);
        Event savedEvent = eventRepository.save(event);
        EventChangedEvent eventChangedEvent = eventChangedEventMapper.toEventChanged(savedEvent, changedBy);
        kafkaChangeEventProducer.sendToChangeEventTopic(eventChangedEvent);
        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + id + " not found."));
        eventRepository.delete(event);
    }

}
