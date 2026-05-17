package com.example.task.dto.task;

import com.example.task.model.Status;

import java.time.LocalDateTime;

public record TaskRequest(
        String name,
        String description,
        Status status,
        LocalDateTime timeEndTask
) {
}
