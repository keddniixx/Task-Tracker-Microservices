package com.example.task.repository;

import com.example.task.model.TaskRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRedisRepository extends CrudRepository<TaskRedis, Long> {
}
