package com.server.notification.service;

import com.server.notification.model.ProcessedEvent;
import com.server.notification.model.TaskDTO;
import com.server.notification.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final ProcessedEventRepository processedEventRepository;

    @KafkaListener(topics = "task", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeTask(TaskDTO task, @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        if (messageKey == null || messageKey.isBlank()) {
            log.warn("Ошибочный ключ {}", messageKey);
            return;
        }
        try {
            ProcessedEvent processedEvent = ProcessedEvent.builder().eventId(messageKey).build();
            processedEventRepository.save(processedEvent);
        } catch (DataIntegrityViolationException e) {
            log.warn("Дубликат сообщения {}", messageKey);
            return;
        }
        log.info("Получена задача в notification service {}", task);
    }
}
