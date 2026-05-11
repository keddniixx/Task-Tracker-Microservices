package com.example.task.service;

import com.example.task.dto.task.TaskDTO;
import com.example.task.model.OutboxEvent;
import com.example.task.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class OutboxEventPoller {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, TaskDTO> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void pollAndSendEvents() {
        List<OutboxEvent> events = outboxEventRepository.findAll();
        if (events.isEmpty()) {
            return;
        }
        log.info("Обработка событий Outbox: count={}", events.size());
        for (OutboxEvent event : events) {
            try {
                TaskDTO taskDTO = objectMapper.readValue(event.getPayload(), TaskDTO.class);
                String eventKey = event.getId() != null ? event.getId().toString() : event.getAggregateId();
                kafkaTemplate.send("task", eventKey, taskDTO);
            } catch (JsonProcessingException e) {
                log.error("Error parsing outbox payload", e);
            }
        }
        outboxEventRepository.deleteAllInBatch(events);
    }
}
