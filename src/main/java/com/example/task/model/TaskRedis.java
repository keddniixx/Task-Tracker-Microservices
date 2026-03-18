package com.example.task.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Task")
public class TaskRedis {
    @Id
    private Long id;
    private String name;
    private String description;
    private Status status;
    private LocalDateTime timeStartTask;
    private LocalDateTime timeEndTask;
}
