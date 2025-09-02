package my.ddos.controller.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ddos.model.dto.EventBooking;
import my.ddos.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingKafkaConsumer {
    public static final String BOOKING_TOPIC = "booking-topic";

    private final NotificationService notificationService;


    @KafkaListener(topics = BOOKING_TOPIC, containerFactory = "kafkaListenerBookingContainerFactory")
    public void consumeBookingEvent(EventBooking eventBooking){
        log.info("Received booking event to notification {}", eventBooking);
        notificationService.save(eventBooking);
    }
}
