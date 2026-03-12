package com.example.task.dto.task;

import com.example.task.model.Status;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record TaskRequest(
        @NotBlank(message = "Name is required")
        String name,
        String description,
        Status status,
        LocalDateTime timeEndTask
) {
}
