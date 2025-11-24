package com.ddos.profile.service;

import com.ddos.profile.dto.ProfileResponse;
import com.ddos.profile.dto.UpdateProfileRequest;
import com.ddos.profile.mapper.ProfileDtoMapper;
import com.ddos.profile.model.Profile;
import com.ddos.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileDtoMapper profileDtoMapper;

    public ProfileResponse getProfileByUserId(Long userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
        return profileDtoMapper.toResponse(profile);
    }

    public ProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        profileDtoMapper.updateProfileFromRequest(request, profile);
        Profile updatedProfile = profileRepository.save(profile);
        return profileDtoMapper.toResponse(updatedProfile);
    }
}
