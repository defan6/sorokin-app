package com.ddos.auth.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "auths")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "full_name", nullable = false)
    private String fullName;


    @Column(nullable = false)
    private String password;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "auth_roles",
            joinColumns = @JoinColumn(name = "auth_id")
    )
    private Set<String> roles = new HashSet<>();
}
