package com.asos.demo.service;

import com.asos.demo.dto.auth.FounderProfileResponse;
import com.asos.demo.dto.auth.LoginRequest;
import com.asos.demo.dto.auth.RegisterRequest;
import com.asos.demo.entity.Founder;
import com.asos.demo.exception.DuplicateResourceException;
import com.asos.demo.exception.InvalidCredentialsException;
import com.asos.demo.exception.ResourceNotFoundException;
import com.asos.demo.repository.FounderRepository;
import com.asos.demo.util.PasswordUtil;
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
