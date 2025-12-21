package com.hahn.tasks.task_manager_backend.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository repository;

    public ProjectController(ProjectRepository repository) {
        this.repository = repository;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        project.setId(null);
        Project saved = repository.save(project);
        return ResponseEntity.ok(saved);
    }

    // READ all
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(repository.findAll());
    }

    // READ one
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return ResponseEntity.ok(project);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id,
                                                 @RequestBody Project updated) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (updated.getName() != null) {
            project.setName(updated.getName());
        }
        if (updated.getDescription() != null) {
            project.setDescription(updated.getDescription());
        }
        if (updated.getStartDate() != null) {
            project.setStartDate(updated.getStartDate());
        }
        if (updated.getEndDate() != null) {
            project.setEndDate(updated.getEndDate());
        }

        Project saved = repository.save(project);
        return ResponseEntity.ok(saved);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
