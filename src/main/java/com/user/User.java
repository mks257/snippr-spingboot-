package com.snippr.api.user;

import java.time.Instant;

public class User {
    private Long id;
    private String email;
    private String passwordHash;
    private Instant createdAt;

    public User() {}
    public User(Long id, String email, String passwordHash, Instant createdAt) {
        this.id = id; this.email = email; this.passwordHash = passwordHash; this.createdAt = createdAt;
    }

    public Long getId() { return id; }              public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }      public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; } public void setPasswordHash(String h) { this.passwordHash = h; }
    public Instant getCreatedAt() { return createdAt; }      public void setCreatedAt(Instant c) { this.createdAt = c; }
}
