package com.example.task.service;

import com.example.task.model.UserLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisInvalidation {

    @CacheEvict(cacheNames = "tasksList", key = "#userLogin.username")
    public void cacheEvictList(UserLogin userLogin) {
        log.info("Очистка кэша {}", userLogin.getUsername());
    }
}
