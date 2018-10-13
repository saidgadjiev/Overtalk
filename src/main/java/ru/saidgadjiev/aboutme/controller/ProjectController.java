package ru.saidgadjiev.aboutme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.Project;
import ru.saidgadjiev.aboutme.model.ProjectDetails;
import ru.saidgadjiev.aboutme.service.ProjectService;
import ru.saidgadjiev.aboutme.utils.DTOUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/project")
public class ProjectController {

    @Autowired
    private ProjectService service;

    @GetMapping(value = "")
    public ResponseEntity<List<Project>> getAll() throws SQLException {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/create")
    public ResponseEntity<ProjectDetails> create(@RequestBody ProjectDetails projectDetails, BindingResult bindingResult) throws IOException, SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Project project = service.create(projectDetails);

        return ResponseEntity.ok(DTOUtils.convert(project, ProjectDetails.class));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/update/{id}")
    public ResponseEntity update(
            @PathVariable("id") Integer id,
            @RequestBody ProjectDetails projectDetails,
            BindingResult bindingResult
    ) throws IOException, SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Project project = service.update(id, projectDetails);

        if (project == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(DTOUtils.convert(project, ProjectDetails.class));
    }
}
