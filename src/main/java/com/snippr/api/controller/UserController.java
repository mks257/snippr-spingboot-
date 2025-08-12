package com.snippr.api.controller;

import com.snippr.api.dto.CreateUserRequest;
import com.snippr.api.user.UnauthorizedException;
import com.snippr.api.user.User;
import com.snippr.api.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<UserService.SanitizedUser> register(@Valid @RequestBody CreateUserRequest body) {
        User created = service.register(body.getEmail(), body.getPassword());
        return ResponseEntity.created(URI.create("/users")).body(service.sanitize(created));
    }

    // Protected: Authorization: Basic base64(email:password)
    @GetMapping
    public UserService.SanitizedUser me(@RequestHeader(value = "Authorization", required = false) String auth) {
        if (auth == null || !auth.startsWith("Basic ")) throw new UnauthorizedException("Missing Basic auth header");
        String decoded = new String(Base64.getDecoder().decode(auth.substring("Basic ".length())), StandardCharsets.UTF_8);
        int idx = decoded.indexOf(":");
        if (idx <= 0) throw new UnauthorizedException("Malformed Basic credentials");
        String email = decoded.substring(0, idx);
        String password = decoded.substring(idx + 1);
        return service.sanitize(service.authenticate(email, password));
    }
}
