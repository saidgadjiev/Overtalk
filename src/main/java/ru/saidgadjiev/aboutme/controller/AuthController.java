package ru.saidgadjiev.aboutme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.Role;
import ru.saidgadjiev.aboutme.model.UserRequest;
import ru.saidgadjiev.aboutme.service.SecurityService;
import ru.saidgadjiev.aboutme.service.UserService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * Created by said on 18.03.2018.
 */
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final UserService userService;

    private final SecurityService securityService;

    @Autowired
    public AuthController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @PostMapping(value = "/signUp")
    public ResponseEntity<org.springframework.security.core.userdetails.UserDetails> signUp(
            @RequestBody @Valid UserRequest userRequest, BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        if (userService.isExistUserName(userRequest.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        securityService.login(userRequest.getUserName(), userRequest.getPassword());

        return ResponseEntity.ok(securityService.findLoggedInUser());
    }

    @GetMapping(value = "/account")
    public ResponseEntity<org.springframework.security.core.userdetails.UserDetails> getAccount() {
        return ResponseEntity.ok(securityService.findLoggedInUser());
    }
}
