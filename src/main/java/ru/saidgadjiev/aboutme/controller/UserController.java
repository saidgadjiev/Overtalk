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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.saidgadjiev.aboutme.domain.UserProfile;
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
            @PageableDefault(page = 0, size = 10, sort = "userName", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        List<UserProfile> userProfiles = userService.getAll(page.getPageSize(), page.getOffset());
        long total = userService.countOff();
        List<ObjectNode> content = new ArrayList<>();

        for (UserProfile userProfile : userProfiles) {
            content.add(new UserJsonBuilder()
                    .nickName(userProfile.getNickName())
                    .userName(userProfile.getUserName())
                    .roles(userProfile.getUserRoles())
                    .build());
        }

        return ResponseEntity.ok(new PageImpl<>(content, page, total));
    }

    @GetMapping(value = "/userName/exist/{userName}")
    public ResponseEntity existUserName(@PathVariable(value = "userName") String userName) throws SQLException {
        if (userService.isExistUserName(userName)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/nickName/exist/{nickName}")
    public ResponseEntity existNickName(@PathVariable(value = "nickName") String nickName) throws SQLException {
        if (userService.isExistNickName(nickName)) {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

        return ResponseEntity.notFound().build();
    }
}
