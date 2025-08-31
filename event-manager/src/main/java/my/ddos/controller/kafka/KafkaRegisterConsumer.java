package my.ddos.controller.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.ddos.event.EventRegisterUser;
import my.ddos.service.user.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static my.ddos.config.kafka.KafkaTopicConfig.REGISTER_USER_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaRegisterConsumer {

    private final UserService userService;

    @KafkaListener(topics = REGISTER_USER_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void saveNewUser(EventRegisterUser eventRegisterUser){
        userService.save(eventRegisterUser);
        log.info("Saved new user {}", eventRegisterUser);
    }
}
