package com.example.task.service;

import com.example.task.dto.task.TaskRequest;
import com.example.task.dto.task.TaskResponse;
import com.example.task.exception.ContentNotFoundException;
import com.example.task.mapper.MapperDTO;
import com.example.task.model.Status;
import com.example.task.model.Task;
import com.example.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final MapperDTO mapper;

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

    public TaskResponse createTask(TaskRequest taskRequest) {
        log.info("Создание задачи {}", taskRequest.name());
        Task task = mapper.toTask(taskRequest);
        task.setTimeStartTask(LocalDateTime.now());
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        Task saved = taskRepository.save(task);
        return mapper.toTaskResponse(saved);
    }

    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
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
        Task updated = taskRepository.save(task);
        return mapper.toTaskResponse(updated);
    }

    public void deleteTask(Long id) {
        log.info("Удаление задачи {}", id);
        if (!taskRepository.existsById(id)) {
            throw new ContentNotFoundException("Task with id " + id + " not found");
        }
        taskRepository.deleteById(id);
    }
}
