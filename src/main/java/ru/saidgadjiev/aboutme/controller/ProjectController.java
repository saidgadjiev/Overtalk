package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.saidgadjiev.aboutme.domain.Project;
import ru.saidgadjiev.aboutme.model.JsonViews;
import ru.saidgadjiev.aboutme.model.ProjectDetails;
import ru.saidgadjiev.aboutme.service.ProjectService;
import ru.saidgadjiev.aboutme.storage.StorageService;
import ru.saidgadjiev.aboutme.utils.DTOUtils;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/project")
public class ProjectController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private ProjectService service;

    @Autowired
    private javax.validation.Validator validator;

    @GetMapping(value = "")
    public ResponseEntity<List<Project>> getAll() throws SQLException {
        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/create")
    public ResponseEntity<ProjectDetails> create(@RequestPart(value = "file", required = false) MultipartFile file,
                                 @RequestPart("data") String data) throws IOException, SQLException {
        ProjectDetails projectDetails = new ObjectMapper().readValue(data, ProjectDetails.class);

        if (hasErrors(projectDetails)) {
            return ResponseEntity.badRequest().build();
        }
        if (file != null) {
            String logoPath = storageService.store(file);

            projectDetails.setLogoPath(logoPath);
        }
        Project project = service.create(projectDetails);

        return ResponseEntity.ok(DTOUtils.convert(project, ProjectDetails.class));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/update/{id}")
    public ResponseEntity update(
            @PathVariable("id") Integer id,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("data") String data
    ) throws IOException, SQLException {
        ProjectDetails projectDetails = new ObjectMapper().readValue(data, ProjectDetails.class);

        if (hasErrors(projectDetails)) {
            return ResponseEntity.badRequest().build();
        }
        if (file != null) {
            String logoPath = storageService.store(file);

            projectDetails.setLogoPath(logoPath);
        }
        Project project = service.update(id, projectDetails);

        if (project == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(DTOUtils.convert(project, ProjectDetails.class));
    }

    private boolean hasErrors(ProjectDetails project) {
        Set<ConstraintViolation<ProjectDetails>> validations = validator.validate(project);

        return !validations.isEmpty();
    }
}
