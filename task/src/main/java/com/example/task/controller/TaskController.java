package com.example.task.controller;

import com.example.task.dto.task.TaskRequest;
import com.example.task.dto.task.TaskResponse;
import com.example.task.model.UserLogin;
import com.example.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/summary")
    public void scheduleSendSummery() {
        taskService.sendSummary();
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> lookAllUserTasks(@AuthenticationPrincipal UserLogin userLogin) {
        return ResponseEntity.ok().body(taskService.lookAllUserTasks(userLogin));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> lookOneUserTasks(@PathVariable("taskId") Long taskId,
                                                         @AuthenticationPrincipal UserLogin userLogin) {
        return ResponseEntity.ok().body(taskService.lookOneUserTasks(userLogin, taskId));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest,
                                                   @AuthenticationPrincipal UserLogin userLogin) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskRequest, userLogin));
    }

    @PutMapping("/edit/{taskId}")
    public ResponseEntity<TaskResponse> editTask(@PathVariable("taskId") Long taskId,
                                                 @RequestBody TaskRequest taskRequest,
                                                 @AuthenticationPrincipal UserLogin userLogin) {
        return ResponseEntity.ok(taskService.editTask(taskRequest, userLogin, taskId));
    }

    @DeleteMapping("/remove/{taskId}")
    public ResponseEntity<TaskResponse> removeTask(@PathVariable("taskId") Long taskId,
                                                   @AuthenticationPrincipal UserLogin userLogin) {
        taskService.removeTask(taskId, userLogin);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/random")
    public ResponseEntity<TaskResponse> randomTask() {
        return ResponseEntity.ok().body(taskService.randomTask());
    }
}
