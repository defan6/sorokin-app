package com.ddos.profile.service;

import com.ddos.profile.client.FileStorageClient;
import com.ddos.profile.dto.CreateProfileRequest;
import com.ddos.profile.dto.ProfileResponse;
import com.ddos.profile.dto.SetProfilePhotoRequest;
import com.ddos.profile.dto.UpdateProfileRequest;
import com.ddos.profile.mapper.ProfileMapper;
import com.ddos.profile.model.Profile;
import com.ddos.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class DefaultProfileService implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    private final FileStorageClient fileStorageClient;

    public ProfileResponse getProfileByUserId(Long userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile with id %d not found".formatted(userId)));
        return profileMapper.toResponse(profile);
    }

    public ProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found".formatted(userId)));

        profileMapper.updateProfileFromRequest(request, profile);
        Profile updatedProfile = profileRepository.save(profile);
        return profileMapper.toResponse(updatedProfile);
    }


    public void createProfile(CreateProfileRequest createProfileRequest) {
        Profile profile = profileMapper.toProfile(createProfileRequest);
        profileRepository.save(profile);
    }

    @Override
    public ProfileResponse setProfilePhoto(Long userId, SetProfilePhotoRequest request) {
        return profileRepository.findById(userId)
                .map(profile -> {
                    profile.setAvatarUrl(request.photoUrl());
                    Profile savedProfile = profileRepository.save(profile);
                    return profileMapper.toResponse(savedProfile);
                })
                .orElseThrow(() -> new RuntimeException("Profile not found".formatted(userId)));
    }
}
