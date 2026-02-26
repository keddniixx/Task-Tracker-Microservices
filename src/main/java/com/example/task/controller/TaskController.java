package com.example.task.controller;

import com.example.task.model.Task;
import com.example.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks() {
        System.out.println("GET /tasks");
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        System.out.println("GET /tasks/" + id);
        return taskService.getTaskById(id);
    }

    @PostMapping
    public String createTask(@RequestBody Task task) {
        if (task.getName() == null || task.getName().trim().isEmpty()) {
            throw new RuntimeException("Task name cannot be empty");
        }
        System.out.println("POST /tasks: " + task.getName());
        taskService.createTask(task);
        return "Task created";
    }

    @PutMapping("/{id}")
    public String updateTask(@PathVariable Long id, @RequestBody Task task) {
        if (task.getName() == null || task.getName().trim().isEmpty()) {
            throw new RuntimeException("Task name cannot be empty");
        }
        System.out.println("PUT /tasks/" + id);
        taskService.updateTask(id, task);
        return "Task updated";
    }

    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        System.out.println("DELETE /tasks/" + id);
        taskService.deleteTask(id);
        return "Task deleted";
    }
}
