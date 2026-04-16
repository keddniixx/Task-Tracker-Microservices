package com.example.task.service;

import com.example.task.dto.task.TaskDTO;
import com.example.task.mapper.MapperDTO;
import com.example.task.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, TaskDTO> kafkaTemplate;
    private final MapperDTO mapper;

    public void sendTaskToKafka(Task task) {
        log.info("Отправка задачи в Kafka {}", task);
        TaskDTO dto = mapper.toTaskDTO(task);
        kafkaTemplate.send("task", String.valueOf(task.getId()), dto);
    }

    public void sendListTaskToKafka(List<TaskDTO> tasks) {
        log.info("Отправка списка задач в Kafka {}", tasks);
        for (TaskDTO task : tasks) {
            kafkaTemplate.send("task", String.valueOf(task.getId()), task);
        }
    }
}
