package com.ddos.profile.controller;

import com.ddos.profile.dto.ProfileResponse;
import com.ddos.profile.dto.UpdateProfileRequest;
import com.ddos.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(@AuthenticationPrincipal Long currentUserId) {
        return ResponseEntity.ok(profileService.getProfileByUserId(currentUserId));
    }

    @PutMapping("/me")
    public ResponseEntity<ProfileResponse> updateMyProfile(
            @AuthenticationPrincipal Long currentUserId,
            @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(currentUserId, request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse> getProfileById(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }
}
