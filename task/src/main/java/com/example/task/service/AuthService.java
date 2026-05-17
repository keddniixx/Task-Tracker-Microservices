package com.example.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    public boolean validateToken(String token) {
        log.info("Валидация токена {}", token);
        return token != null && !token.isBlank();
    }
}
