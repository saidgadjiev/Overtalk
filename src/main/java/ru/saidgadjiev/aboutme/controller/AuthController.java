package ru.saidgadjiev.aboutme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.Role;
import ru.saidgadjiev.aboutme.model.ResponseMessage;
import ru.saidgadjiev.aboutme.model.UserDetails;
import ru.saidgadjiev.aboutme.service.SecurityService;
import ru.saidgadjiev.aboutme.service.UserService;
import ru.saidgadjiev.aboutme.utils.ErrorUtils;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * Created by said on 18.03.2018.
 */
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    private UserService userService;

    private SecurityService securityService;

    @Autowired
    public AuthController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<?> signUp(@RequestBody @Valid UserDetails userDetails, BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseMessage<>().setContent(ErrorUtils.toErrors(bindingResult)));
        }
        if (userService.isExists(userDetails.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        userDetails.setRoles(userService.create(userDetails).stream().map(Role::getName).collect(Collectors.toSet()));
        securityService.login(userDetails.getUserName(), userDetails.getPassword());

        return ResponseEntity.ok(new ResponseMessage<>().setContent(securityService.findLoggedInUserName()));
    }
}
