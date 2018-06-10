package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.saidgadjiev.aboutme.domain.Project;
import ru.saidgadjiev.aboutme.model.PostDetails;
import ru.saidgadjiev.aboutme.model.ProjectDetails;
import ru.saidgadjiev.aboutme.model.ResponseMessage;
import ru.saidgadjiev.aboutme.service.ProjectService;
import ru.saidgadjiev.aboutme.storage.StorageService;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/project")
public class ProjectController {

    private static final Logger LOGGER = Logger.getLogger(ProjectController.class);

    @Autowired
    private ProjectService service;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResponseMessage<List<ProjectDetails>>> getAll() throws SQLException {
        LOGGER.debug("getAll()");

        return ResponseEntity.ok(new ResponseMessage<>("", service.getAll()));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody @Valid ProjectDetails projectDetails) throws IOException, SQLException {
        LOGGER.debug("create()");
        service.create(projectDetails);
        LOGGER.debug("Added new project " + projectDetails);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Project> getProject(@PathVariable("id") Integer id) throws IOException {
        LOGGER.debug("getProject()");

        return ResponseEntity.ok(new Project());
    }
}
