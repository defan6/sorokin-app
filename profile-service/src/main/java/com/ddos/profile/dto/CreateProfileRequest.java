package com.ddos.profile.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProfileRequest {
    private Long userId;
    private String username;
    private String fullName;
    private String photoUrl;
    private String address;
}
