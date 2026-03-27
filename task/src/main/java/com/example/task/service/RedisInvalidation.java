package com.example.task.service;

import com.example.task.model.UserLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisInvalidation {
    private final RedisTemplate<String, Object> redisTemplate;

    public void cacheEvictList(UserLogin userLogin) {
        if (userLogin != null && userLogin.getUsername() != null) {
            redisTemplate.delete("tasksList::" + userLogin.getUsername());
        }
    }
}
