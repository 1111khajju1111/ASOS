package com.asos.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manual session authentication filter. Plain servlet Filter
 * (org.springframework.web.filter.OncePerRequestFilter is part of spring-web,
 * NOT Spring Security) - intentionally avoiding the Spring Security framework
 * and avoiding JWTs. Relies on the container's HttpSession (JSESSIONID cookie)
 * set by AuthController on login/register.
 */
public class SessionAuthFilter extends OncePerRequestFilter {

    public static final String FOUNDER_ID_SESSION_KEY = "founderId";

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/actuator/health"
    );

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String path = request.getRequestURI();
        return PUBLIC_PATHS.stream().anyMatch(path::equals);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Object founderId = session != null ? session.getAttribute(FOUNDER_ID_SESSION_KEY) : null;

        if (founderId == null) {
            sendUnauthorized(response, "Not signed in, or your session has expired");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> body = new HashMap<>();
        body.put("status", 401);
        body.put("error", "Unauthorized");
        body.put("message", message);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
