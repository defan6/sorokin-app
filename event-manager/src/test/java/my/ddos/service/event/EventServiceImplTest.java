package my.ddos.service.event;

import jakarta.persistence.EntityManager;
import my.ddos.exception.EventNotFoundException;
import my.ddos.exception.UserNotFoundException;
import my.ddos.exception.VenueNotFoundException;
import my.ddos.mapper.EventMapper;
import my.ddos.model.dto.event.EventRequest;
import my.ddos.model.dto.event.EventResponse;
import my.ddos.model.dto.user.UserResponse;
import my.ddos.model.dto.venue.VenueResponse;
import my.ddos.model.entity.Event;
import my.ddos.model.entity.User;
import my.ddos.model.entity.Venue;
import my.ddos.repository.EventRepository;
import my.ddos.service.user.UserService;
import my.ddos.service.venue.VenueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventMapper eventMapper;
    @Mock
    private UserService userService;
    @Mock
    private VenueService venueService;
    @Mock
    private EntityManager entityManager;

    // Unused mocks for now, can be added when testing other methods
    // @Mock private KafkaChangeEventProducer kafkaChangeEventProducer;
    // @Mock private EventValidator eventValidator;
    // @Mock private EventChangedEventMapper eventChangedEventMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void getEvent_shouldReturnEventResponse_whenEventExists() {
        // Given
        Long eventId = 1L;
        Event event = new Event();
        event.setId(eventId);
        event.setTitle("Test Event");

        EventResponse eventResponse = new EventResponse(eventId, "Test Event", "Description", LocalDateTime.of(2025, 12, 14, 15, 35, 25), 100L, 1L);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventMapper.toResponse(event)).thenReturn(eventResponse);

        // When
        EventResponse actualResponse = eventService.getEvent(eventId);

        // Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.id()).isEqualTo(eventId);
        assertThat(actualResponse.title()).isEqualTo("Test Event");

        verify(eventRepository, times(1)).findById(eventId);
        verify(eventMapper, times(1)).toResponse(event);
    }

    @Test
    void getEvent_shouldThrowEventNotFoundException_whenEventDoesNotExist() {
        // Given
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EventNotFoundException.class, () -> eventService.getEvent(eventId));

        verify(eventRepository, times(1)).findById(eventId);
    }

    @Test
    void getAllEvents_shouldReturnListOfEventResponses_whenEventsExist() {
        // Given
        Event event1 = new Event();
        event1.setId(1L);
        Event event2 = new Event();
        event2.setId(2L);
        List<Event> events = List.of(event1, event2);

        EventResponse response1 = new EventResponse(1L, "Event 1", "Desc 1", LocalDateTime.now(), 10L, 100L);
        EventResponse response2 = new EventResponse(2L, "Event 2", "Desc 2", LocalDateTime.now(), 20L, 200L);

        when(eventRepository.findAll()).thenReturn(events);
        when(eventMapper.toResponse(event1)).thenReturn(response1);
        when(eventMapper.toResponse(event2)).thenReturn(response2);

        // When
        List<EventResponse> actualResponses = eventService.getAllEvents();

        // Then
        assertThat(actualResponses).isNotNull().hasSize(2);
        verify(eventRepository, times(1)).findAll();
        verify(eventMapper, times(1)).toResponse(event1);
        verify(eventMapper, times(1)).toResponse(event2);
    }

    @Test
    void getAllEvents_shouldReturnEmptyList_whenNoEventsExist() {
        // Given
        when(eventRepository.findAll()).thenReturn(List.of());

        // When
        List<EventResponse> actualResponses = eventService.getAllEvents();

        // Then
        assertThat(actualResponses).isNotNull().isEmpty();
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void createEvent_shouldCreateEventSuccessfully() {
        // Given
        String username = "organizer";
        Long organizerId = 10L;
        Long venueId = 1L;
        EventRequest eventRequest = new EventRequest("New Event", "New Description", LocalDateTime.now(), venueId);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(organizerId);
        userResponse.setUsername(username);
        userResponse.setFullName("Organizer Name");
        userResponse.setCreatedAt(LocalDateTime.now());

        VenueResponse venueResponse = new VenueResponse(venueId, "Venue Name", "Venue Address", 100L);

        Event eventToSave = new Event();
        User userProxy = new User();
        Venue venueProxy = new Venue();
        Event savedEvent = new Event();
        savedEvent.setId(1L);

        EventResponse finalResponse = new EventResponse(1L, "New Event", "New Description", eventRequest.eventDate(), venueId, organizerId);

        when(userService.getInfoAboutCurrentUser(username)).thenReturn(userResponse);
        when(venueService.getVenue(venueId)).thenReturn(venueResponse);
        when(eventMapper.toEntity(eventRequest)).thenReturn(eventToSave);
        when(entityManager.getReference(User.class, organizerId)).thenReturn(userProxy);
        when(entityManager.getReference(Venue.class, venueId)).thenReturn(venueProxy);
        when(eventRepository.save(eventToSave)).thenReturn(savedEvent);
        when(eventMapper.toResponse(savedEvent)).thenReturn(finalResponse);

        // When
        EventResponse actualResponse = eventService.createEvent(eventRequest, username);

        // Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.id()).isEqualTo(1L);
        assertThat(eventToSave.getOrganizer()).isEqualTo(userProxy);
        assertThat(eventToSave.getVenue()).isEqualTo(venueProxy);

        verify(userService, times(1)).getInfoAboutCurrentUser(username);
        verify(venueService, times(1)).getVenue(venueId);
        verify(entityManager, times(1)).getReference(User.class, organizerId);
        verify(entityManager, times(1)).getReference(Venue.class, venueId);
        verify(eventRepository, times(1)).save(eventToSave);
    }

    @Test
    void createEvent_shouldThrowException_whenUserNotFound() {
        // Given
        String username = "nonexistent";
        Long venueId = 1L;
        EventRequest eventRequest = new EventRequest("New Event", "New Description", LocalDateTime.now(), venueId);

        when(eventMapper.toEntity(eventRequest)).thenReturn(new Event()); // Fix added here
        when(userService.getInfoAboutCurrentUser(username)).thenThrow(new UserNotFoundException("User not found"));

        // When & Then
        assertThrows(UserNotFoundException.class, () -> eventService.createEvent(eventRequest, username));

        verify(userService, times(1)).getInfoAboutCurrentUser(username);
        verify(venueService, never()).getVenue(anyLong());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void createEvent_shouldThrowException_whenVenueNotFound() {
        // Given
        String username = "organizer";
        Long venueId = 99L; // Non-existent
        Long organizerId = 10L;
        EventRequest eventRequest = new EventRequest("New Event", "New Description", LocalDateTime.now(), venueId);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(organizerId);
        userResponse.setUsername(username);
        userResponse.setFullName("Organizer Name");
        userResponse.setCreatedAt(LocalDateTime.now());

        when(userService.getInfoAboutCurrentUser(username)).thenReturn(userResponse);
        when(entityManager.getReference(User.class, organizerId)).thenReturn(new User()); // Fix added here
        when(venueService.getVenue(venueId)).thenThrow(new VenueNotFoundException("Venue not found"));
        when(eventMapper.toEntity(eventRequest)).thenReturn(new Event()); // Fix added here

        // When & Then
        assertThrows(VenueNotFoundException.class, () -> eventService.createEvent(eventRequest, username));

        verify(userService, times(1)).getInfoAboutCurrentUser(username);
        verify(venueService, times(1)).getVenue(venueId);
        verify(eventRepository, never()).save(any(Event.class));
    }
}
