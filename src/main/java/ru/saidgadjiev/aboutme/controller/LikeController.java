package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.Like;
import ru.saidgadjiev.aboutme.json.LikeJsonBuilder;
import ru.saidgadjiev.aboutme.service.LikeService;

import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/like/post/{id}")
    public ResponseEntity<ObjectNode> like(
            @PathVariable("id") Integer postId
    ) throws SQLException {
        Like like = likeService.postLike(postId);

        ObjectNode response = new LikeJsonBuilder()
                .postId(like.getPost().getId())
                .likesCount(likeService.postLikesCount(postId))
                .liked(true)
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/dislike/post/{id}")
    public ResponseEntity<ObjectNode> dislike(
            @PathVariable("id") Integer postId
    ) throws SQLException {
        Like like = likeService.postDislike(postId);

        ObjectNode objectNode = new LikeJsonBuilder()
                .postId(like.getPost().getId())
                .likesCount(likeService.postLikesCount(postId))
                .liked(false)
                .build();

        return ResponseEntity.ok(objectNode);
    }
}
