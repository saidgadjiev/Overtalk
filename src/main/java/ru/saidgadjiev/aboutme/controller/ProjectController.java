package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.saidgadjiev.aboutme.domain.Project;
import ru.saidgadjiev.aboutme.model.ProjectDetails;
import ru.saidgadjiev.aboutme.model.ResponseMessage;
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
    public ResponseEntity<ResponseMessage<List<ProjectDetails>>> getAll() throws SQLException {
        LOGGER.debug("getAll()");

        return ResponseEntity.ok(new ResponseMessage<>("", service.getAll()));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity create(@RequestPart(value="file", required = false) MultipartFile file,
                                 @RequestPart("data") String data) throws IOException, SQLException {
        LOGGER.debug("create()");
        ProjectDetails projectDetails = new ObjectMapper().readValue(data, ProjectDetails.class);
        if (file != null) {
            String logoPath = storageService.store(file);

            projectDetails.setLogoPath(logoPath);
        }
        service.create(projectDetails);
        LOGGER.debug("Added new project " + projectDetails);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage<ProjectDetails>> update(@RequestPart(value="file", required = false) MultipartFile file,
                                 @RequestPart("data") String data) throws IOException, SQLException {
        LOGGER.debug("create()");
        ProjectDetails projectDetails = new ObjectMapper().readValue(data, ProjectDetails.class);
        if (file != null) {
            String logoPath = storageService.store(file);

            projectDetails.setLogoPath(logoPath);
        }
        int count = service.update(projectDetails);
        LOGGER.debug("Update project " + count);

        return ResponseEntity.ok(new ResponseMessage<>("", projectDetails));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Project> getProject(@PathVariable("id") Integer id) throws IOException {
        LOGGER.debug("getProject()");

        return ResponseEntity.ok(new Project());
    }
}
