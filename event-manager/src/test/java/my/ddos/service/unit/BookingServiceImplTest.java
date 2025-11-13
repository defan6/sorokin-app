package my.ddos.service.unit;

import jakarta.persistence.EntityManager;
import my.ddos.controller.kafka.KafkaBookingProducer;
import my.ddos.enums.BookingStatus;
import my.ddos.exception.BookingNotFoundException;
import my.ddos.exception.EventNotFoundException;
import my.ddos.mapper.BookingMapper;
import my.ddos.mapper.EventBookingMapper;
import my.ddos.model.dto.booking.*;
import my.ddos.model.dto.event.EventResponse;
import my.ddos.model.dto.kafka.EventBooking;
import my.ddos.model.dto.user.UserResponse;
import my.ddos.model.entity.Booking;
import my.ddos.model.entity.Event;
import my.ddos.model.entity.User;
import my.ddos.repository.BookingRepository;
import my.ddos.service.booking.BookingServiceImpl;
import my.ddos.service.event.EventService;
import my.ddos.service.user.UserService;
import my.ddos.validator.BookingValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private EventService eventService;
    @Mock
    private UserService userService;
    @Mock
    private EntityManager entityManager;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private EventBookingMapper eventBookingMapper;
    @Mock
    private KafkaBookingProducer kafkaBookingProducer;
    @Mock
    private BookingValidator bookingValidator;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void getMyBookings_shouldReturnUserBookings_whenUserHasBookings() {
        // Given
        String username = "testuser";
        Booking booking1 = new Booking();
        booking1.setId(1L);
        Booking booking2 = new Booking();
        booking2.setId(2L);
        List<Booking> bookings = List.of(booking1, booking2);
        MyBookingResponse response1 = new MyBookingResponse(1L, "Event 1", null, "Venue 1", "REGISTERED");
        MyBookingResponse response2 = new MyBookingResponse(2L, "Event 2", null, "Venue 2", "REGISTERED");
        List<MyBookingResponse> bookingResponses = List.of(response1, response2);
        UserBookingResponse expectedResponse = new UserBookingResponse(username, bookingResponses);

        when(bookingRepository.findAllByUserUsername(username)).thenReturn(bookings);
        when(bookingMapper.toMyBookingResponse(booking1)).thenReturn(response1);
        when(bookingMapper.toMyBookingResponse(booking2)).thenReturn(response2);
        when(bookingMapper.toUserBookingResponse(eq(username), anyList())).thenReturn(expectedResponse);

        // When
        UserBookingResponse actualResponse = bookingService.getMyBookings(username);

        // Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.username()).isEqualTo(username);
        assertThat(actualResponse.myBookingResponses()).hasSize(2);
        verify(bookingRepository).findAllByUserUsername(username);
        verify(bookingMapper, times(2)).toMyBookingResponse(any(Booking.class));
        verify(bookingMapper).toUserBookingResponse(eq(username), anyList());
    }

    @Test
    void getMyBookings_shouldReturnEmptyList_whenUserHasNoBookings() {
        // Given
        String username = "testuser";
        when(bookingRepository.findAllByUserUsername(username)).thenReturn(Collections.emptyList());
        when(bookingMapper.toUserBookingResponse(username, Collections.emptyList()))
                .thenReturn(new UserBookingResponse(username, Collections.emptyList()));

        // When
        UserBookingResponse actualResponse = bookingService.getMyBookings(username);

        // Then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.username()).isEqualTo(username);
        assertThat(actualResponse.myBookingResponses()).isEmpty();
        verify(bookingRepository).findAllByUserUsername(username);
        verify(bookingMapper, never()).toMyBookingResponse(any(Booking.class));
        verify(bookingMapper).toUserBookingResponse(username, Collections.emptyList());
    }

    @Test
    void getAllBookings_shouldReturnAllBookingsGroupedByUser() {
        // Given
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");

        Booking booking1 = new Booking();
        booking1.setUser(user1);
        Booking booking2 = new Booking();
        booking2.setUser(user2);
        Booking booking3 = new Booking();
        booking3.setUser(user1);

        List<Booking> allBookings = List.of(booking1, booking2, booking3);

        MyBookingResponse response1 = new MyBookingResponse(1L, "Event 1", null, "Venue 1", "REGISTERED");
        MyBookingResponse response2 = new MyBookingResponse(2L, "Event 2", null, "Venue 2", "REGISTERED");
        MyBookingResponse response3 = new MyBookingResponse(3L, "Event 3", null, "Venue 3", "REGISTERED");

        when(bookingRepository.findAll()).thenReturn(allBookings);
        when(bookingMapper.toMyBookingResponse(booking1)).thenReturn(response1);
        when(bookingMapper.toMyBookingResponse(booking2)).thenReturn(response2);
        when(bookingMapper.toMyBookingResponse(booking3)).thenReturn(response3);

        // When
        List<UserBookingResponse> result = bookingService.getAllBookings();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.stream().map(UserBookingResponse::username).toList()).containsExactlyInAnyOrder("user1", "user2");
        verify(bookingRepository).findAll();
        verify(bookingMapper, times(3)).toMyBookingResponse(any(Booking.class));
    }

    @Test
    void getAllBookings_shouldReturnEmptyList_whenNoBookingsExist() {
        // Given
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<UserBookingResponse> result = bookingService.getAllBookings();

        // Then
        assertThat(result).isEmpty();
        verify(bookingRepository).findAll();
        verify(bookingMapper, never()).toMyBookingResponse(any(Booking.class));
    }

    @Test
    void createBooking_shouldCreateBooking_whenRequestIsValid() {
        // Given
        String username = "testuser";
        long eventId = 1L;
        long userId = 10L;
        BookingRequest bookingRequest = new BookingRequest(eventId);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setUsername(username);

        EventResponse eventResponse = new EventResponse(eventId, "Event", "Desc", LocalDateTime.now(), 1L, 1L);

        Booking savedBooking = new Booking();
        savedBooking.setId(1L);
        RegisterBookingResponse expectedResponse = new RegisterBookingResponse(1L, "Test Event", username, BookingStatus.REGISTERED, null, "Success");

        doNothing().when(bookingValidator).validateBookingRequest(bookingRequest, username);
        when(userService.getInfoAboutCurrentUser(username)).thenReturn(userResponse);
        when(eventService.getEvent(eventId)).thenReturn(eventResponse);
        when(entityManager.getReference(eq(User.class), anyLong())).thenReturn(new User());
        when(entityManager.getReference(eq(Event.class), anyLong())).thenReturn(new Event());
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
        when(bookingMapper.toRegisterBookingResponse(nullable(String.class), any(Booking.class))).thenReturn(expectedResponse);
        when(eventBookingMapper.toEventBooking(anyString(), nullable(String.class), any(Booking.class))).thenReturn(new EventBooking(null, 1L, BookingStatus.REGISTERED, null, username));


        // When
        RegisterBookingResponse actualResponse = bookingService.createBooking(bookingRequest, username);

        // Then
        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(bookingValidator).validateBookingRequest(bookingRequest, username);
        verify(userService).getInfoAboutCurrentUser(username);
        verify(eventService).getEvent(eventId);
        verify(bookingRepository).save(any(Booking.class));
        verify(kafkaBookingProducer).sendToBookingTopic(any(EventBooking.class));
    }

    @Test
    void createBooking_shouldThrowEventNotFoundException_whenEventNotFound() {
        // Given
        String username = "testuser";
        long eventId = 1L;
        BookingRequest bookingRequest = new BookingRequest(eventId);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername(username);

        doNothing().when(bookingValidator).validateBookingRequest(bookingRequest, username);
        when(userService.getInfoAboutCurrentUser(username)).thenReturn(userResponse);
        when(eventService.getEvent(eventId)).thenThrow(new EventNotFoundException("Event not found"));

        // When & Then
        assertThatThrownBy(() -> bookingService.createBooking(bookingRequest, username))
                .isInstanceOf(EventNotFoundException.class);

        verify(bookingRepository, never()).save(any(Booking.class));
        verify(kafkaBookingProducer, never()).sendToBookingTopic(any(EventBooking.class));
    }

    @Test
    void createBooking_shouldThrowException_whenValidationFails() {
        // Given
        String username = "testuser";
        long eventId = 1L;
        BookingRequest bookingRequest = new BookingRequest(eventId);
        RuntimeException validationException = new RuntimeException("Validation failed");

        doThrow(validationException).when(bookingValidator).validateBookingRequest(bookingRequest, username);

        // When & Then
        assertThatThrownBy(() -> bookingService.createBooking(bookingRequest, username))
                .isInstanceOf(RuntimeException.class)
                .isEqualTo(validationException);

        verify(userService, never()).getInfoAboutCurrentUser(anyString());
        verify(eventService, never()).getEvent(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
        verify(kafkaBookingProducer, never()).sendToBookingTopic(any(EventBooking.class));
    }

    @Test
    void cancelBooking_shouldCancelBooking_whenRequestIsValid() {
        // Given
        String username = "testuser";
        long bookingId = 1L;
        long userId = 10L;
        CancelBookingRequest cancelRequest = new CancelBookingRequest(bookingId);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setUsername(username);

        User user = new User();
        user.setId(userId);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setUser(user);
        booking.setBookingStatus(BookingStatus.REGISTERED);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userService.getInfoAboutCurrentUser(username)).thenReturn(userResponse);
        when(eventBookingMapper.toEventBooking(anyString(), nullable(String.class), any(Booking.class))).thenReturn(new EventBooking(null, 1L, BookingStatus.CANCELLED, null, username));

        // When
        bookingService.cancelBooking(username, cancelRequest);

        // Then
        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).save(bookingCaptor.capture());
        Booking savedBooking = bookingCaptor.getValue();

        assertThat(savedBooking.getBookingStatus()).isEqualTo(BookingStatus.CANCELLED);
        verify(kafkaBookingProducer).sendToBookingTopic(any(EventBooking.class));
    }

    @Test
    void cancelBooking_shouldThrowBookingNotFoundException_whenBookingNotFound() {
        // Given
        String username = "testuser";
        long bookingId = 1L;
        CancelBookingRequest cancelRequest = new CancelBookingRequest(bookingId);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookingService.cancelBooking(username, cancelRequest))
                .isInstanceOf(BookingNotFoundException.class)
                .hasMessage("Booking with id " + bookingId + " not found");

        verify(bookingRepository, never()).save(any(Booking.class));
        verify(kafkaBookingProducer, never()).sendToBookingTopic(any(EventBooking.class));
    }

    @Test
    void cancelBooking_shouldThrowBookingNotFoundException_whenUserIsNotOwner() {
        // Given
        String username = "testuser";
        long bookingId = 1L;
        CancelBookingRequest cancelRequest = new CancelBookingRequest(bookingId);

        User owner = new User();
        owner.setId(1L);
        owner.setUsername("owner");

        UserResponse nonOwnerResponse = new UserResponse();
        nonOwnerResponse.setId(2L);
        nonOwnerResponse.setUsername(username);

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setUser(owner);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userService.getInfoAboutCurrentUser(username)).thenReturn(nonOwnerResponse);

        // When & Then
        assertThatThrownBy(() -> bookingService.cancelBooking(username, cancelRequest))
                .isInstanceOf(BookingNotFoundException.class)
                .hasMessage("Booking with id " + bookingId + " not found");

        verify(bookingRepository, never()).save(any(Booking.class));
        verify(kafkaBookingProducer, never()).sendToBookingTopic(any(EventBooking.class));
    }
}
