package com.ddos.profile.event;

import com.ddos.profile.mapper.ProfileMapper;
import com.ddos.profile.model.Profile;
import com.ddos.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileEventConsumer {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @KafkaListener(topics = "user-registered-topic", groupId = "profile-service-group")
    public void listenUserRegisteredEvent(UserRegisteredEvent event) {
        log.info("Received UserRegisteredEvent: {}", event);

        Profile profile = profileMapper.toProfile(event);

        profileRepository.save(profile);
        log.info("Profile created for userId: {}", event.userId());
    }
}