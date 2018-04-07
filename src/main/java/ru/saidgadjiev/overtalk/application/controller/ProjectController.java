package ru.saidgadjiev.overtalk.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.saidgadjiev.overtalk.application.model.PostDetails;
import ru.saidgadjiev.overtalk.application.model.ProjectDetails;
import ru.saidgadjiev.overtalk.application.model.ResponseMessage;
import ru.saidgadjiev.overtalk.application.service.ProjectService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping(value = "/api")
public class ProjectController {

    private static final Logger LOGGER = Logger.getLogger(ProjectController.class);

    @Autowired
    private ProjectService service;

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public ResponseEntity getAll() throws SQLException {
        LOGGER.debug("getAll()");

        return ResponseEntity.ok(new ResponseMessage<>().setContent(service.getAll()));
    }

    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public ResponseEntity create(@RequestParam(value = "file") MultipartFile file, @RequestPart(value = "data") String data) throws IOException {
        PostDetails postDetails = new ObjectMapper().readValue(data, PostDetails.class);
        LOGGER.debug("create()");

        return ResponseEntity.ok("ok");
    }
}
