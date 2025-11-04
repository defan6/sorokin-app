package my.ddos.controller.rest;

import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.NotificationResponse;
import my.ddos.model.entity.Notification;
import my.ddos.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notificator/my")
public class NotificatorController {


    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(@RequestHeader("X-Username") String username){
        return ResponseEntity.ok(notificationService.getAllMyNotifications(username));
    }


    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getMyUnreadNotifications(@RequestHeader("X-Username") String username){
        return ResponseEntity.ok(notificationService.getMyUnreadNotifications(username));
    }

    @PostMapping("/mark-as-read/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void markNotificationAsRead(@PathVariable Long id, @RequestHeader("X-Username") String username){
        notificationService.markNotificationAsRead(id, username);
    }
}
