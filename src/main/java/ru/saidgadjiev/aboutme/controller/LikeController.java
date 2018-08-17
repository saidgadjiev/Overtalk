package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.saidgadjiev.aboutme.json.LikeJsonBuilder;
import ru.saidgadjiev.aboutme.service.LikeService;

import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/like/post/{id}")
    public ResponseEntity<ObjectNode> like(
            @PathVariable("id") Integer postId
    ) throws SQLException {
        likeService.postLike(postId);

        ObjectNode response = new LikeJsonBuilder()
                .postId(postId)
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
        likeService.postDislike(postId);

        ObjectNode objectNode = new LikeJsonBuilder()
                .postId(postId)
                .likesCount(likeService.postLikesCount(postId))
                .liked(false)
                .build();

        return ResponseEntity.ok(objectNode);
    }
}
