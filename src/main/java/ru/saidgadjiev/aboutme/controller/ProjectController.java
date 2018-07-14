package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.DataBinder;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.saidgadjiev.aboutme.domain.Project;
import ru.saidgadjiev.aboutme.service.ProjectService;
import ru.saidgadjiev.aboutme.storage.StorageService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/project")
public class ProjectController {

    private static final Logger LOGGER = Logger.getLogger(ProjectController.class);

    @Autowired
    private StorageService storageService;

    @Autowired
    private ProjectService service;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Project>> getAll() throws SQLException {
        LOGGER.debug("getPostsByCategoryId()");

        return ResponseEntity.ok(service.getAll());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity create(@RequestPart(value="file", required = false) MultipartFile file,
                                 @RequestPart("data") String data) throws IOException, SQLException {
        LOGGER.debug("create()");
        Project project = new ObjectMapper().readValue(data, Project.class);

        if (hasErrors(project)) {
            return ResponseEntity.badRequest().build();
        }
        if (file != null) {
            String logoPath = storageService.store(file);

            project.setLogoPath(logoPath);
        }
        service.create(project);
        LOGGER.debug("Added new project " + project);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<Project> update(@RequestPart(value="file", required = false) MultipartFile file,
                                 @RequestPart("data") String data) throws IOException, SQLException {
        LOGGER.debug("create()");
        Project project = new ObjectMapper().readValue(data, Project.class);

        if (hasErrors(project)) {
            return ResponseEntity.badRequest().build();
        }
        if (file != null) {
            String logoPath = storageService.store(file);

            project.setLogoPath(logoPath);
        }
        int count = service.update(project);
        LOGGER.debug("Update project " + count);

        return ResponseEntity.ok(project);
    }

    private boolean hasErrors(Project project) {
        DataBinder dataBinder = new DataBinder(project);

        dataBinder.setValidator(new OptionalValidatorFactoryBean());
        dataBinder.validate();

        return dataBinder.getBindingResult().hasErrors();
    }
}
