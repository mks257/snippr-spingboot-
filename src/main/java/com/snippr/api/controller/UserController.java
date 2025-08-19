package com.snippr.api.controller;

import com.snippr.api.dto.CreateUserRequest;
import com.snippr.api.user.UnauthorizedException;
import com.snippr.api.user.User;
import com.snippr.api.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

    // GET /users with Authorization: Bearer <jwt>
    @GetMapping
    public UserService.SanitizedUser me(HttpServletRequest req) {
        String email = (String) req.getAttribute(com.snippr.api.security.TokenAuthInterceptor.REQ_EMAIL_ATTR);
        if (email == null) throw new UnauthorizedException("Unauthorized");
        return service.sanitize(service.findByEmail(email));
    }
}
