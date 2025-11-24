package com.ddos.profile.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private String bio;
    private String avatarUrl;
}
