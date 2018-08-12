package ru.saidgadjiev.aboutme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.model.LikeDetails;
import ru.saidgadjiev.aboutme.service.LikeService;

import javax.validation.Valid;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/like/post/{id}")
    public ResponseEntity<LikeDetails> like(
            @PathVariable("id") Integer postId
    ) throws SQLException {
        return ResponseEntity.ok(likeService.postLike(postId));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/dislike/post/{id}")
    public ResponseEntity<LikeDetails> dislike(
            @PathVariable("id") Integer id
    ) throws SQLException {
        return ResponseEntity.ok(likeService.postDislike(id));
    }
}
