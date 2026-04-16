package com.example.task.service;

import com.example.task.dto.task.TaskRequest;
import com.example.task.dto.task.TaskResponse;
import com.example.task.exception.ContentNotFoundException;
import com.example.task.mapper.MapperDTO;
import com.example.task.model.Status;
import com.example.task.model.Task;
import com.example.task.model.UserLogin;
import com.example.task.repository.TaskRedisRepository;
import com.example.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskRedisRepository taskRedisRepository;
    private final MapperDTO mapper;
    private final RedisInvalidation redisInvalidation;
    private final KafkaService kafkaService;

    @Cacheable(cacheNames = "tasksList")
    public List<TaskResponse> getAllTasks() {
        log.info("Получение всех задач");
        return taskRepository.findAll().stream().map(mapper::toTaskResponse).toList();
    }

    @Cacheable(cacheNames = "task", key = "#id")
    public TaskResponse getTaskById(Long id) {
        log.info("Получение задачи по id {}", id);
        return taskRepository.findById(id).map(mapper::toTaskResponse)
                .orElseThrow(() -> new ContentNotFoundException("Task with id " + id + " not found"));
    }

    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest, UserLogin userLogin) {
        log.info("Создание задачи {}", taskRequest.name());
        Task task = mapper.toTask(taskRequest);
        task.setTimeStartTask(LocalDateTime.now());
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        Task saved = taskRepository.save(task);
        redisInvalidation.cacheEvictList(userLogin);
        kafkaService.sendTaskToKafka(saved);
        return mapper.toTaskResponse(saved);
    }

    @Transactional
    @CacheEvict(cacheNames = "task", key = "#id")
    public TaskResponse updateTask(Long id, TaskRequest taskRequest, UserLogin userLogin) {
        log.info("Обновление задачи {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException("Task with id " + id + " not found"));
        if (taskRequest.name() != null) {
            task.setName(taskRequest.name());
        }
        if (taskRequest.description() != null) {
            task.setDescription(taskRequest.description());
        }
        if (taskRequest.status() != null) {
            task.setStatus(taskRequest.status());
        }
        if (taskRequest.timeEndTask() != null) {
            task.setTimeEndTask(taskRequest.timeEndTask());
        }
        redisInvalidation.cacheEvictList(userLogin);
        Task updated = taskRepository.save(task);
        return mapper.toTaskResponse(updated);
    }

    @Transactional
    @CacheEvict(cacheNames = "task", key = "#id")
    public void deleteTask(Long id, UserLogin userLogin) {
        log.info("Удаление задачи {}", id);
        if (!taskRepository.existsById(id)) {
            throw new ContentNotFoundException("Task with id " + id + " not found");
        }
        redisInvalidation.cacheEvictList(userLogin);
        taskRepository.deleteById(id);
    }

    public TaskResponse randomTask() {
        Random random = new Random();
        long count = taskRepository.count();
        if (count == 0) {
            throw new ContentNotFoundException("No tasks available");
        }
        long randomLong = random.nextLong(1, count + 1);

        return taskRedisRepository.findById(randomLong)
                .map(cached -> {
                    TaskResponse response = TaskResponse.builder()
                            .id(cached.getId())
                            .status(cached.getStatus())
                            .description(cached.getDescription())
                            .name(cached.getName())
                            .timeStartTask(cached.getTimeStartTask())
                            .timeEndTask(cached.getTimeEndTask())
                            .build();
                    log.info("Из кэша {}", response);
                    return response;
                })
                .orElseGet(() -> {
                    Task task = taskRepository.findById(randomLong)
                            .orElseThrow(() -> new ContentNotFoundException("Task not found"));
                    taskRedisRepository.save(mapper.toRedisTask(task));
                    return mapper.toTaskResponse(task);
                });
    }
}
