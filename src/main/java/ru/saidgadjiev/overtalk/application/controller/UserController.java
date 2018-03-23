package ru.saidgadjiev.overtalk.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.saidgadjiev.overtalk.application.model.ResponseMessage;
import ru.saidgadjiev.overtalk.application.model.UserDetails;
import ru.saidgadjiev.overtalk.application.service.SecurityService;
import ru.saidgadjiev.overtalk.application.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.SQLException;

/**
 * Created by said on 18.03.2018.
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private UserService userService;

    private SecurityService securityService;

    @Autowired
    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> signUp(@Valid UserDetails userDetails, BindingResult bindingResult) throws SQLException {
        if (userService.isExists(userDetails.getUserName())) {
            return ResponseEntity.ok(new ResponseMessage(HttpServletResponse.SC_CONFLICT, "User already exists"));
        }
        userService.create(userDetails);
        securityService.login(userDetails.getUserName(), userDetails.getPassword());

        return ResponseEntity.ok(new ResponseMessage(HttpServletResponse.SC_OK, "user.created"));
    }
}
