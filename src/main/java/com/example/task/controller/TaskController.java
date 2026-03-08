package com.example.task.controller;

import com.example.task.model.Task;
import com.example.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        log.info("GET /tasks");
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        log.info("GET /tasks/{}", id);
        return taskService.getTaskById(id);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        if (task.getName() == null || task.getName().trim().isEmpty()) {
            throw new RuntimeException("Task name cannot be empty");
        }
        log.info("POST /tasks");
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        if (task.getName() == null || task.getName().trim().isEmpty()) {
            throw new RuntimeException("Task name cannot be empty");
        }
        log.info("PUT /tasks/{}", id);
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        log.info("DELETE /tasks/{}", id);
        taskService.deleteTask(id);
    }
}
