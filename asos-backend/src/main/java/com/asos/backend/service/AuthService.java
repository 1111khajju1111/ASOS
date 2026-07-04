package com.asos.backend.service;

import com.asos.backend.dto.auth.FounderProfileResponse;
import com.asos.backend.dto.auth.LoginRequest;
import com.asos.backend.dto.auth.RegisterRequest;
import com.asos.backend.entity.Founder;
import com.asos.backend.exception.DuplicateResourceException;
import com.asos.backend.exception.InvalidCredentialsException;
import com.asos.backend.exception.ResourceNotFoundException;
import com.asos.backend.repository.FounderRepository;
import com.asos.backend.util.PasswordUtil;
import org.springframework.stereotype.Service;

/**
 * Handles founder registration/login. Session creation (HttpSession) is
 * handled in AuthController, since that's where the HttpServletRequest lives -
 * this service stays framework-agnostic and just deals with Founder data.
 */
@Service
public class AuthService {

    private final FounderRepository founderRepository;
    private final PasswordUtil passwordUtil;

    public AuthService(FounderRepository founderRepository, PasswordUtil passwordUtil) {
        this.founderRepository = founderRepository;
        this.passwordUtil = passwordUtil;
    }

    public Founder register(RegisterRequest request) {
        if (founderRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        String hashedPassword = passwordUtil.hash(request.getPassword());
        Founder founder = new Founder(request.getFullName(), request.getEmail(), hashedPassword, request.getCompanyName());
        return founderRepository.save(founder);
    }

    public Founder login(LoginRequest request) {
        Founder founder = founderRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordUtil.matches(request.getPassword(), founder.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return founder;
    }

    public FounderProfileResponse getProfile(Long founderId) {
        Founder founder = founderRepository.findById(founderId)
                .orElseThrow(() -> new ResourceNotFoundException("Founder not found"));

        return new FounderProfileResponse(founder.getId(), founder.getFullName(), founder.getEmail(),
                founder.getCompanyName(), founder.getCreatedAt());
    }
}
