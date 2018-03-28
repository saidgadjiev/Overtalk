package ru.saidgadjiev.overtalk.application.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.saidgadjiev.overtalk.application.model.CommentDetails;
import ru.saidgadjiev.overtalk.application.model.ResponseMessage;
import ru.saidgadjiev.overtalk.application.model.UserDetails;
import ru.saidgadjiev.overtalk.application.service.UserService;

import java.sql.SQLException;

/**
 * Created by said on 25.03.2018.
 */
@RestController
@RequestMapping(value = "/api")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity getAll(
            @PageableDefault(page = 0, size = 10, sort = "userName", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        LOGGER.debug("getAll(): " + page);
        Page<UserDetails> userDetails = userService.getAll(page);

        return ResponseEntity.ok(new ResponseMessage<>().setContent(userDetails));
    }
}
