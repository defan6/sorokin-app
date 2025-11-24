package com.ddos.profile.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    private Long userId;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(length = 200)
    private String fullName;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 255)
    private String avatarUrl;

    @Column(length = 255)
    private String address;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
