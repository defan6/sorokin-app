package my.ddos.service;

import my.ddos.model.dto.ChangeNotificationStatusRequest;
import my.ddos.model.dto.EventBooking;
import my.ddos.model.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getAllMyNotifications(String username);

    List<NotificationResponse> getMyUnreadNotifications(String username);

    void markNotificationAsRead(ChangeNotificationStatusRequest request, String username);


    void save(EventBooking eventBooking);
}
