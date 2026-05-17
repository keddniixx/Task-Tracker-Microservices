package com.server.scheduler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledService {

    @Value("${gateway.url:http://gateway:8222/api/v1/task/summary}")
    private String gatewayUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(cron = "*/10 * * * * *")
    public void sendTaskSummaryNotification() {
        try {
            restTemplate.getForEntity(gatewayUrl, Void.class);
            log.info("Запрос отправлен");
        } catch (Exception e) {
            log.warn("Ошибка запроса");
        }
    }
}
