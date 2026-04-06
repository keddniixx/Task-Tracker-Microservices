package com.server.notification.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskDTO(
        String username,
        String name,
        String description,
        LocalDateTime timeStartTask,
        LocalDateTime timeEndTask,
        Status status
) {
}
