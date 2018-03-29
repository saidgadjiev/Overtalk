package ru.saidgadjiev.overtalk.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.overtalk.application.domain.Role;
import ru.saidgadjiev.overtalk.application.model.ResponseMessage;
import ru.saidgadjiev.overtalk.application.model.UserDetails;
import ru.saidgadjiev.overtalk.application.service.SecurityService;
import ru.saidgadjiev.overtalk.application.service.UserService;
import ru.saidgadjiev.overtalk.application.utils.ErrorUtils;

import javax.servlet.http.HttpServletResponse;
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

    @RequestMapping(value = "/exist", method = RequestMethod.GET)
    public ResponseEntity<?> exist(@RequestParam(value = "userName") String userName) throws SQLException {
        if (userService.isExists(userName)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

        return ResponseEntity.notFound().build();
    }
}
