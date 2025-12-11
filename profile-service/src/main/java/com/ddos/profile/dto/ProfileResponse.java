package com.ddos.profile.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileResponse {
    private Long userId;
    private String username;
    private String fullName;
    private String bio;
    private String address;
    private String avatarUrl;
}

