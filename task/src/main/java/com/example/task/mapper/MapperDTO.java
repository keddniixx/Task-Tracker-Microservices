package com.example.task.mapper;

import com.example.task.dto.task.TaskDTO;
import com.example.task.dto.task.TaskRequest;
import com.example.task.dto.task.TaskResponse;
import com.example.task.model.Task;
import com.example.task.model.TaskRedis;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapperDTO {
    Task toTask(TaskRequest taskRequest);
    TaskResponse toTaskResponse(Task task);
    TaskRedis toRedisTask(Task task);
    TaskDTO toTaskDTO(Task task);
}
