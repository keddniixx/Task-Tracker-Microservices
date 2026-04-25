package com.server.security.service;

import com.server.security.dto.LoginRequestDTO;
import com.server.security.dto.LoginResponse;
import com.server.security.repository.UserRepository;
import com.server.security.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public Optional<LoginResponse> login(LoginRequestDTO loginRequestDTO) {
        return userRepository.findByUsername(loginRequestDTO.username())
                .filter(u -> passwordEncoder.matches(loginRequestDTO.password(), u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getUsername(), u.getRole()))
                .map(t -> LoginResponse.builder().token(t).build());
    }

    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
