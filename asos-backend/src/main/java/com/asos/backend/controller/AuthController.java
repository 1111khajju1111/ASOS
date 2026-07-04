package com.asos.backend.controller;

import com.asos.backend.dto.auth.AuthResponse;
import com.asos.backend.dto.auth.FounderProfileResponse;
import com.asos.backend.dto.auth.LoginRequest;
import com.asos.backend.dto.auth.RegisterRequest;
import com.asos.backend.dto.common.MessageResponse;
import com.asos.backend.entity.Founder;
import com.asos.backend.security.CurrentFounderId;
import com.asos.backend.security.SessionAuthFilter;
import com.asos.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request,
                                                  HttpServletRequest httpRequest) {
        Founder founder = authService.register(request);
        startSession(httpRequest, founder.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toAuthResponse(founder));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                               HttpServletRequest httpRequest) {
        Founder founder = authService.login(request);
        startSession(httpRequest, founder.getId());
        return ResponseEntity.ok(toAuthResponse(founder));
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(new MessageResponse("Signed out"));
    }

    @GetMapping("/me")
    public ResponseEntity<FounderProfileResponse> me(@CurrentFounderId Long founderId) {
        return ResponseEntity.ok(authService.getProfile(founderId));
    }

    private void startSession(HttpServletRequest httpRequest, Long founderId) {
        HttpSession session = httpRequest.getSession(true); // create if absent
        session.setAttribute(SessionAuthFilter.FOUNDER_ID_SESSION_KEY, founderId);
    }

    private AuthResponse toAuthResponse(Founder founder) {
        return new AuthResponse(founder.getId(), founder.getFullName(), founder.getEmail(), founder.getCompanyName());
    }
}
