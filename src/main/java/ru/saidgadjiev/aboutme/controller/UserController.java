package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.Userprofile;
import ru.saidgadjiev.aboutme.json.UserJsonBuilder;
import ru.saidgadjiev.aboutme.service.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by said on 25.03.2018.
 */
@RestController
@RequestMapping(value = "/api")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users")
    public ResponseEntity<Page<ObjectNode>> getAll(
            @PageableDefault(page = 0, size = 10, sort = "username", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        List<Userprofile> userprofiles = userService.getAll(page.getPageSize(), page.getOffset());
        long total = userService.countOff();
        List<ObjectNode> content = new ArrayList<>();

        for (Userprofile userprofile : userprofiles) {
            content.add(new UserJsonBuilder()
                    .nickname(userprofile.getNickname())
                    .username(userprofile.getUsername())
                    .roles(userprofile.getUserprofileRoles())
                    .build());
        }

        return ResponseEntity.ok(new PageImpl<>(content, page, total));
    }

    @RequestMapping(value = "/username/exist/{username}", method = RequestMethod.HEAD)
    public ResponseEntity existUserName(@PathVariable(value = "username") String username) throws SQLException {
        if (userService.isExistUserName(username)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/nickname/exist/{nickname}", method = RequestMethod.HEAD)
    public ResponseEntity existNickName(@PathVariable(value = "nickname") String nickname) throws SQLException {
        if (userService.isExistNickName(nickname)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

        return ResponseEntity.ok().build();
    }
}
