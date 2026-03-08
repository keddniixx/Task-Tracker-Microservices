package com.example.task.service;

import com.example.task.model.Task;
import com.example.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        log.info("Получение всех задач");
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        log.info("Получение задачи по id {}", id);
        return taskRepository.findById(id).orElse(null);
    }

    public Task createTask(Task task) {
        log.info("Создание задачи {}", task.getName());
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        log.info("Обновление задачи {}", id);
        task.setId(id);
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        log.info("Удаление задачи {}", id);
        taskRepository.deleteById(id);
    }
}
