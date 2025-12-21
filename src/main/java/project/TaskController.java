package com.hahn.tasks.task_manager_backend.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class TaskController {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public TaskController(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    // 1) إضافة task لمشروع
    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<Task> addTaskToProject(@PathVariable Long projectId,
                                                 @RequestBody Task task) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        task.setId(null);
        task.setProject(project);
        Task saved = taskRepository.save(task);
        return ResponseEntity.ok(saved);
    }

    // 2) جلب جميع الـ tasks ديال مشروع
    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<Task>> getTasksForProject(@PathVariable Long projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }

    // 3) تحديث حالة task
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId,
                                           @RequestBody Task updated) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (updated.getTitle() != null) {
            task.setTitle(updated.getTitle());
        }
        if (updated.getDescription() != null) {
            task.setDescription(updated.getDescription());
        }
        task.setCompleted(updated.isCompleted());

        Task saved = taskRepository.save(task);
        return ResponseEntity.ok(saved);
    }

    // 4) حذف task
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskRepository.deleteById(taskId);
        return ResponseEntity.noContent().build();
    }

    // 5) progress ديال المشروع
    @GetMapping("/{projectId}/progress")
    public ResponseEntity<ProjectProgressResponse> getProjectProgress(@PathVariable Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        long total = taskRepository.countByProjectId(projectId);
        long completed = taskRepository.countByProjectIdAndCompletedTrue(projectId);

        double percentage = total == 0 ? 0.0 : (completed * 100.0) / total;

        ProjectProgressResponse response = new ProjectProgressResponse(
                project.getId(),
                project.getName(),
                total,
                completed,
                percentage
        );

        return ResponseEntity.ok(response);
    }

    public static class ProjectProgressResponse {
        private Long projectId;
        private String projectName;
        private long totalTasks;
        private long completedTasks;
        private double percentage;

        public ProjectProgressResponse(Long projectId, String projectName,
                                       long totalTasks, long completedTasks,
                                       double percentage) {
            this.projectId = projectId;
            this.projectName = projectName;
            this.totalTasks = totalTasks;
            this.completedTasks = completedTasks;
            this.percentage = percentage;
        }

        public Long getProjectId() { return projectId; }
        public String getProjectName() { return projectName; }
        public long getTotalTasks() { return totalTasks; }
        public long getCompletedTasks() { return completedTasks; }
        public double getPercentage() { return percentage; }
    }
}
