package com.ddos.profile.service;

import com.ddos.profile.dto.CreateProfileRequest;
import com.ddos.profile.dto.ProfileResponse;
import com.ddos.profile.dto.SetProfilePhotoRequest;
import com.ddos.profile.dto.UpdateProfileRequest;

public interface ProfileService {

    ProfileResponse getProfileByUserId(Long userId);

    ProfileResponse updateProfile(Long userId, UpdateProfileRequest request);

    void createProfile(CreateProfileRequest createProfileRequest);


    ProfileResponse setProfilePhoto(Long userId, SetProfilePhotoRequest request);



}
