package com.carlosmurilo.tickethub.user.dto;

import com.carlosmurilo.tickethub.user.UserRole;
import java.time.Instant;
import java.util.UUID;

public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private UserRole role;
    private Instant createdAt;

    public UserResponse(UUID id, String name, String email, UserRole role, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public UserRole getRole() { return role; }
    public Instant getCreatedAt() { return createdAt; }
}