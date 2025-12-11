package com.ddos.profile.controller;

import com.ddos.profile.dto.ProfileResponse;
import com.ddos.profile.dto.SetProfilePhotoRequest;
import com.ddos.profile.dto.UpdateProfileRequest;
import com.ddos.profile.service.DefaultProfileService;
import com.ddos.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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


    @PostMapping("/me/photo")
    public ResponseEntity<ProfileResponse> uploadProfilePhoto(
            @AuthenticationPrincipal Long currentUserId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(profileService.uploadProfilePhoto(currentUserId, file));
    }


    @GetMapping
    public ResponseEntity<List<ProfileResponse>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @PatchMapping("/set-photo-url")
    public ResponseEntity<ProfileResponse> setPhotoUrl(@AuthenticationPrincipal Long currentUserId,
                                                       @RequestBody SetProfilePhotoRequest request) {
        return ResponseEntity.ok(profileService.setProfilePhoto(currentUserId, request));
    }


    @DeleteMapping("/me/photo")
    public ResponseEntity<Void> deletePhoto(@AuthenticationPrincipal Long currentUserId){
        profileService.deleteProfilePhoto(currentUserId);
        return ResponseEntity.noContent().build();
    }
}
