package ru.saidgadjiev.overtalk.application.controller;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * Created by said on 25.03.2018.
 */
@RestController
@RequestMapping(value = "/api")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity getAll() throws SQLException {
        return ResponseEntity.ok().build();
    }
}
