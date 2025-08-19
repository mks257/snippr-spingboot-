package com.snippr.api.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.snippr.api.user.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenAuthInterceptor implements HandlerInterceptor {
    private final JwtService jwt;

    public static final String REQ_EMAIL_ATTR = "snippr.email";

    public TokenAuthInterceptor(JwtService jwt) { this.jwt = jwt; }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        String path = req.getRequestURI();

        // Which paths are secured? Adjust as needed:
        boolean secured = path.startsWith("/users") || path.startsWith("/snippets");
        if (!secured) return true;

        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer "))
            throw new UnauthorizedException("Missing Bearer token");

        DecodedJWT decoded = jwt.verify(auth.substring(7));
        req.setAttribute(REQ_EMAIL_ATTR, decoded.getSubject()); // the email
        return true;
    }
}
