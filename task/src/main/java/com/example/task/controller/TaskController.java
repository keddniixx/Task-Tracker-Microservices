package com.example.task.controller;

import com.example.task.dto.task.TaskRequest;
import com.example.task.dto.task.TaskResponse;
import com.example.task.model.UserLogin;
import com.example.task.service.TaskService;
import jakarta.validation.Valid;
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
    public List<TaskResponse> getAllTasks() {
        log.info("GET /tasks");
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        log.info("GET /tasks/{}", id);
        return taskService.getTaskById(id);
    }

    @GetMapping("/random")
    public TaskResponse randomTask() {
        log.info("GET /tasks/random");
        return taskService.randomTask();
    }

    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody TaskRequest taskRequest) {
        log.info("POST /tasks");
        return taskService.createTask(taskRequest, new UserLogin("default", "ROLE_USER"));
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest taskRequest) {
        log.info("PUT /tasks/{}", id);
        return taskService.updateTask(id, taskRequest, new UserLogin("default", "ROLE_USER"));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        log.info("DELETE /tasks/{}", id);
        taskService.deleteTask(id, new UserLogin("default", "ROLE_USER"));
    }
}
