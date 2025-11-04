package com.ddos.auth.kafka.controller;


import com.ddos.auth.kafka.event.EventChangedRole;
import com.ddos.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.ddos.auth.config.KafkaTopicConfig.CHANGE_USER_ROLE_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaChangeRoleConsumer {

    private final AuthService authService;

    @KafkaListener(topics = CHANGE_USER_ROLE_TOPIC, containerFactory = "kafkaListenerChangeRoleContainerFactory")
    public void consumeChangeRole(EventChangedRole eventChangedRole){
        log.info("auth service: recieved event change role from {} for {}", CHANGE_USER_ROLE_TOPIC, eventChangedRole.username());
        authService.changeRole(eventChangedRole);
    }
}
