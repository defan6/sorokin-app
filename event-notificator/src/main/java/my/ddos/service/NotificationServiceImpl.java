package my.ddos.service;

import lombok.RequiredArgsConstructor;
import my.ddos.enums.ReadStatus;
import my.ddos.exception.NotificationNotFoundException;
import my.ddos.mapper.BookingEventMapper;
import my.ddos.mapper.NotificationMapper;
import my.ddos.model.dto.ChangeNotificationStatusRequest;
import my.ddos.model.dto.EventBooking;
import my.ddos.model.dto.NotificationResponse;
import my.ddos.model.entity.Notification;
import my.ddos.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final BookingEventMapper bookingEventMapper;


    @Override
    public List<NotificationResponse> getAllMyNotifications(String username) {
        return notificationRepository
                .findAll()
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    public List<NotificationResponse> getMyUnreadNotifications(String username) {
        return notificationRepository
                .findAll()
                .stream()
                .filter(n -> n.getReadStatus().equals(ReadStatus.UNREAD))
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    public void markNotificationAsRead(ChangeNotificationStatusRequest request, String username) {
        Notification notification = notificationRepository.findByIdAndUsername(request.notificationId(), username)
                .orElseThrow(() -> new NotificationNotFoundException("Notification with notificationId " + request.notificationId() + " not found."));
        notification.setReadStatus(ReadStatus.READ);
        notificationRepository.save(notification);
    }

    @Override
    public void save(EventBooking eventBooking) {
        Notification notification = bookingEventMapper.toEntity(eventBooking);
        notificationRepository.save(notification);
    }
}
