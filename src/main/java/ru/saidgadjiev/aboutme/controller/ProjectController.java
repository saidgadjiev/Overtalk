package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.saidgadjiev.aboutme.domain.Project;
import ru.saidgadjiev.aboutme.service.ProjectService;
import ru.saidgadjiev.aboutme.storage.StorageService;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/project")
public class    ProjectController {

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
    public ResponseEntity create(@RequestPart(value="file", required = false) MultipartFile file,
                                 @RequestPart("data") String data) throws IOException, SQLException {
        Project project = new ObjectMapper().readValue(data, Project.class);

        if (hasErrors(project)) {
            return ResponseEntity.badRequest().build();
        }
        if (file != null) {
            String logoPath = storageService.store(file);

            project.setLogoPath(logoPath);
        }
        service.create(project);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/update")
    public ResponseEntity<Project> update(@RequestPart(value="file", required = false) MultipartFile file,
                                 @RequestPart("data") String data) throws IOException, SQLException {
        Project project = new ObjectMapper().readValue(data, Project.class);

        if (hasErrors(project)) {
            return ResponseEntity.badRequest().build();
        }
        if (file != null) {
            String logoPath = storageService.store(file);

            project.setLogoPath(logoPath);
        }
        service.update(project);

        return ResponseEntity.ok(project);
    }

    private boolean hasErrors(Project project) {
        Set<ConstraintViolation<Project>> validations = validator.validate(project);

        return !validations.isEmpty();
    }
}
