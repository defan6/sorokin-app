package com.ddos.profile.service;

import com.ddos.profile.client.FileStorageClient;
import com.ddos.profile.dto.*;
import com.ddos.profile.mapper.ProfileMapper;
import com.ddos.profile.model.Profile;
import com.ddos.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultProfileService implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    private final FileStorageClient fileStorageClient;

    @Value("${app.default-avatar-url}")
    private String defaultAvatarUrl;

    public ProfileResponse getProfileByUserId(Long userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile with id %d not found".formatted(userId)));
        return profileMapper.toResponse(profile);
    }

    public ProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile for user with id: %d not found".formatted(userId)));

        profileMapper.updateProfileFromRequest(request, profile);
        Profile updatedProfile = profileRepository.save(profile);
        return profileMapper.toResponse(updatedProfile);
    }

    public void deleteProfilePhoto(Long userId){
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Profile for user with id: %d not found".formatted(userId)));
        String photoUrl = profile.getAvatarUrl();
        if(!Objects.equals(photoUrl, defaultAvatarUrl)){
            throw new RuntimeException("User with id: %d has default photo.".formatted(userId));
        }
        fileStorageClient.deleteProfilePhoto(userId, photoUrl);
        profile.setAvatarUrl(defaultAvatarUrl);
    }

    @Override
    public List<ProfileResponse> getAllProfiles() {
        return profileRepository.findAll()
                .stream()
                .map(profileMapper::toResponse)
                .toList();
    }


    public void createProfile(CreateProfileRequest createProfileRequest) {
        Profile profile = profileMapper.toProfile(createProfileRequest);
        profile.setAvatarUrl(defaultAvatarUrl);
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



    @Override
    public ProfileResponse uploadProfilePhoto(Long currentUserId, MultipartFile file) {
        Profile profile = profileRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        UploadFileResponse uploadFileResponse = fileStorageClient.uploadFile(file, currentUserId);

        profile.setAvatarUrl(uploadFileResponse.url());
        Profile saved = profileRepository.save(profile);

        return profileMapper.toResponse(saved);
    }



}
