package ru.saidgadjiev.aboutme.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.model.CommentDetails;
import ru.saidgadjiev.aboutme.service.BlogService;

import javax.validation.Valid;
import java.sql.SQLException;

@RestController
@RequestMapping(value = "/api/comment")
public class CommentController {

    private static final Logger LOGGER = Logger.getLogger(CommentController.class);

    @Autowired
    private BlogService blogService;

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.GET)
    public ResponseEntity<Page<CommentDetails>> getCommentsByPost(
            @PathVariable("id") Integer id,
            @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        LOGGER.debug("getCommentsByPost(): " + id + ", " + page);
        Page<CommentDetails> commentDetails = blogService.getCommentsByPostId(id, page);

        return ResponseEntity.ok(commentDetails);
    }

    @RequestMapping(value = "/{id}/create", method = RequestMethod.POST)
    public ResponseEntity createCommentOfPost(
            @PathVariable("id") Integer id,
            @RequestBody @Valid CommentDetails commentDetails
    ) throws SQLException {
        LOGGER.debug("create():" + commentDetails);
        blogService.createCommentOfPost(id, commentDetails);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<CommentDetails> updateComment(
            @RequestBody CommentDetails commentDetails
    ) throws SQLException {
        LOGGER.debug("updateComment():" + commentDetails);
        blogService.updateComment(commentDetails);

        return ResponseEntity.ok(commentDetails);
    }
}
