package com.hahn.tasks.task_manager_backend.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    long countByProjectId(Long projectId);
    long countByProjectIdAndCompletedTrue(Long projectId);
}
