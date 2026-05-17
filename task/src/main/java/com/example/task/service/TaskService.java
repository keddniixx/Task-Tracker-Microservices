package com.example.task.service;

import com.example.task.dto.task.TaskRequest;
import com.example.task.dto.task.TaskResponse;
import com.example.task.exception.ContentNotFoundException;
import com.example.task.mapper.MapperDTO;
import com.example.task.model.Status;
import com.example.task.model.Task;
import com.example.task.model.User;
import com.example.task.model.UserLogin;
import com.example.task.repository.TaskRedisRepository;
import com.example.task.repository.TaskRepository;
import com.example.task.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final MapperDTO mapper;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final KafkaService kafkaService;
    private final TaskRedisRepository taskRedisRepository;
    private final RedisInvalidation redisInvalidation;

    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest, UserLogin userLogin) {
        Task task = mapper.toTask(taskRequest);
        task.setTimeStartTask(LocalDateTime.now());
        task.setStatus(Status.TODO);
        User user = userRepository.findByUsername(userLogin.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        task.setUser(user);
        taskRepository.save(task);
        redisInvalidation.cacheEvictList(userLogin);
        kafkaService.sendTaskToKafka(task);
        return mapper.toTaskResponse(task);
    }

    @Cacheable(cacheNames = "tasksList", key = "#userLogin.username", unless = "#result == null")
    public List<TaskResponse> lookAllUserTasks(UserLogin userLogin) {
        User user = userRepository.findByUsername(userLogin.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        List<Task> tasks = taskRepository.findByUser(user);
        return new ArrayList<>(tasks.stream().map(mapper::toTaskResponse).toList());
    }

    @Transactional
    @CacheEvict(cacheNames = "task", key = "#taskId")
    public void removeTask(Long taskId, UserLogin userLogin) {
        User user = userRepository.findByUsername(userLogin.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        List<Task> tasks = taskRepository.findByUser(user);
        Task taskToRemove = tasks.stream()
                .filter(task -> Objects.equals(task.getId(), taskId))
                .findFirst().orElseThrow(ContentNotFoundException::new);
        redisInvalidation.cacheEvictList(userLogin);
        taskRepository.deleteById(taskToRemove.getId());
    }

    @Transactional
    @CacheEvict(cacheNames = "task", key = "#taskId")
    public TaskResponse editTask(TaskRequest taskRequest, UserLogin userLogin, Long taskId) {
        User user = userRepository.findByUsername(userLogin.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        List<Task> tasks = taskRepository.findByUser(user);
        Task taskToEdit = tasks.stream()
                .filter(task -> Objects.equals(task.getId(), taskId))
                .findFirst().orElseThrow(ContentNotFoundException::new);
        if (taskRequest.name() != null) {
            taskToEdit.setName(taskRequest.name());
        }
        if (taskRequest.timeEndTask() != null) {
            taskToEdit.setTimeEndTask(taskRequest.timeEndTask());
        }
        if (taskRequest.status() != null) {
            taskToEdit.setStatus(taskRequest.status());
        }
        if (taskRequest.description() != null) {
            taskToEdit.setDescription(taskRequest.description());
        }
        log.info("Изменение задачи {}", taskToEdit);
        redisInvalidation.cacheEvictList(userLogin);
        taskRepository.save(taskToEdit);
        return mapper.toTaskResponse(taskToEdit);
    }

    public void sendSummary() {
        List<Task> tasks = taskRepository.findAll();
        Set<User> users = tasks.stream().map(Task::getUser).collect(Collectors.toSet());
        users.stream()
                .map(User::getTasks)
                .forEach(
                        userTasks -> kafkaService.sendListTaskToKafka(userTasks.stream()
                                .map(mapper::toTaskDTO).toList()));
        log.info("Отправка сводки {}", users);
    }

    public TaskResponse randomTask() {
        Random random = new Random();
        long count = taskRepository.count();
        if (count == 0) {
            throw new ContentNotFoundException("No tasks available");
        }
        long randomLong = random.nextLong(1, count + 1);

        return taskRedisRepository.findById(randomLong)
                .map(cached -> {
                    TaskResponse response = TaskResponse.builder()
                            .id(cached.getId())
                            .status(cached.getStatus())
                            .description(cached.getDescription())
                            .name(cached.getName())
                            .timeStartTask(cached.getTimeStartTask())
                            .timeEndTask(cached.getTimeEndTask())
                            .build();
                    log.info("Из кэша {}", response);
                    return response;
                })
                .orElseGet(() -> {
                    Task task = taskRepository.findById(randomLong).orElseThrow(() ->
                            new UsernameNotFoundException("Task not found"));
                    taskRedisRepository.save(mapper.toRedisTask(task));
                    return mapper.toTaskResponse(task);
                });
    }

    @Cacheable(cacheNames = "task", key = "#taskId", unless = "#result == null")
    public TaskResponse lookOneUserTasks(UserLogin userLogin, Long taskId) {
        User user = userRepository.findByUsername(userLogin.getUsername()).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        Task task = taskRepository.findByIdAndUser(taskId, user).orElseThrow(() -> new TaskRejectedException("Task exends"));
        return mapper.toTaskResponse(task);
    }
}
