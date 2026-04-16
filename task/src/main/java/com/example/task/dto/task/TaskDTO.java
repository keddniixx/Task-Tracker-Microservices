package com.example.task.dto.task;

import com.example.task.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String name;
    private String description;
    private Status status;
    private LocalDateTime timeStartTask;
    private LocalDateTime timeEndTask;
}
