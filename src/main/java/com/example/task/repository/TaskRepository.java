package com.example.task.repository;

import com.example.task.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Task> rowMapper = (rs, rowNum) -> new Task(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("status")
    );

    public List<Task> findAll() {
        return jdbcTemplate.query("SELECT id, name, description, status FROM tasks", rowMapper);
    }

    public Task findById(Long id) {
        List<Task> tasks = jdbcTemplate.query("SELECT id, name, description, status FROM tasks WHERE id = ?", rowMapper, id);
        if (tasks.isEmpty()) {
            return null;
        }
        return tasks.get(0);
    }

    public void save(Task task) {
        jdbcTemplate.update("INSERT INTO tasks (name, description, status) VALUES (?, ?, ?)",
                task.getName(), task.getDescription(), task.getStatus());
    }

    public void update(Long id, Task task) {
        jdbcTemplate.update("UPDATE tasks SET name = ?, description = ?, status = ? WHERE id = ?",
                task.getName(), task.getDescription(), task.getStatus(), id);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id);
    }
}
