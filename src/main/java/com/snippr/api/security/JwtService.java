package com.snippr.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtService {
    private final Algorithm alg;
    private final long ttlSeconds;

    public JwtService(
            @Value("") String secretB64,
            @Value("") long ttlSeconds
    ) {
        if (secretB64 == null || secretB64.isBlank())
            throw new IllegalStateException("Missing JWT secret (SNIPPR_JWT_SECRET).");
        byte[] secret = java.util.Base64.getDecoder().decode(secretB64);
        this.alg = Algorithm.HMAC256(secret);
        this.ttlSeconds = ttlSeconds;
    }

    public Token issueFor(String email) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(ttlSeconds);
        String token = JWT.create()
                .withSubject(email)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .withIssuer("snippr-api")
                .sign(alg);
        return new Token(token, exp);
    }

    public DecodedJWT verify(String bearerToken) {
        var verifier = JWT.require(alg).withIssuer("snippr-api").build();
        return verifier.verify(bearerToken);
    }

    public record Token(String value, Instant expiresAt) {}
}