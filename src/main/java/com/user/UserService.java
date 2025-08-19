package com.snippr.api.user;

import com.snippr.api.security.PasswordService;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordService pwd;

    public UserService(UserRepository repo, PasswordService pwd) {
        this.repo = repo; this.pwd = pwd;
    }

    public User register(String email, String rawPassword) {
        repo.findByEmail(email).ifPresent(u -> { throw new IllegalArgumentException("Email already registered"); });
        String hash = pwd.hash(rawPassword);
        User u = new User(null, email, hash, null);
        return repo.save(u);
    }

    public User authenticate(String email, String rawPassword) {
        User u = repo.findByEmail(email).orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
        if (!pwd.matches(rawPassword, u.getPasswordHash())) throw new UnauthorizedException("Invalid credentials");
        return u;
    }

    public SanitizedUser sanitize(User u) {
        return new SanitizedUser(u.getId(), u.getEmail(), u.getCreatedAt());
    }

    public static class SanitizedUser {
        private Long id; private String email; private java.time.Instant createdAt;
        public SanitizedUser(Long id, String email, java.time.Instant createdAt) { this.id=id; this.email=email; this.createdAt=createdAt; }
        public Long getId() { return id; } public String getEmail() { return email; } public java.time.Instant getCreatedAt() { return createdAt; }
    }
    public User findByEmail(String email) {
    return repo.findByEmail(email).orElseThrow(() -> new UnauthorizedException("User not found"));
    }

}
