package com.example.task.dto.task;

import com.example.task.model.Status;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskResponse(
        Long id,
        String name,
        String description,
        Status status,
        LocalDateTime timeStartTask,
        LocalDateTime timeEndTask
) {
}
