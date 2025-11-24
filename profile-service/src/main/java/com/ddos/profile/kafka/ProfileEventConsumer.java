package com.ddos.profile.kafka;

import com.ddos.profile.dto.CreateProfileRequest;
import com.ddos.profile.event.UserRegisteredEvent;
import com.ddos.profile.mapper.ProfileMapper;
import com.ddos.profile.model.Profile;
import com.ddos.profile.repository.ProfileRepository;
import com.ddos.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileEventConsumer {

    private final ProfileMapper profileMapper;
    private final ProfileService profileService;

    @KafkaListener(topics = "register-user-topic", groupId = "profile-service-group")
    public void listenUserRegisteredEvent(UserRegisteredEvent event) {
        log.info("Received UserRegisteredEvent: {}", event);

        CreateProfileRequest createProfileRequest = profileMapper.toProfileRequest(event);
        profileService.createProfile(createProfileRequest);
        log.info("Profile created for userId: {}", event.userId());
    }
}