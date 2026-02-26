package com.example.task.service;

import com.example.task.model.Task;
import com.example.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        System.out.println("fetching all tasks");
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        System.out.println("fetching task by id: " + id);
        return taskRepository.findById(id);
    }

    public void createTask(Task task) {
        System.out.println("task created: " + task.getName());
        taskRepository.save(task);
    }

    public void updateTask(Long id, Task task) {
        System.out.println("task updated: " + id);
        taskRepository.update(id, task);
    }

    public void deleteTask(Long id) {
        System.out.println("task deleted: " + id);
        taskRepository.deleteById(id);
    }
}
