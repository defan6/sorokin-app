package com.ddos.profile.service;

import com.ddos.profile.dto.CreateProfileRequest;
import com.ddos.profile.dto.ProfileResponse;
import com.ddos.profile.dto.SetProfilePhotoRequest;
import com.ddos.profile.dto.UpdateProfileRequest;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfileService {

    ProfileResponse getProfileByUserId(Long userId);

    ProfileResponse updateProfile(Long userId, UpdateProfileRequest request);

    void createProfile(CreateProfileRequest createProfileRequest);


    ProfileResponse setProfilePhoto(Long userId, SetProfilePhotoRequest request);

    ProfileResponse uploadProfilePhoto(Long currentUserId, MultipartFile file);

    void deleteProfilePhoto(Long currentUserId);

    List<ProfileResponse> getAllProfiles();
}
