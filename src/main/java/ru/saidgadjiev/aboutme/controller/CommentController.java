package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.Comment;
import ru.saidgadjiev.aboutme.json.CommentJsonBuilder;
import ru.saidgadjiev.aboutme.model.CommentRequest;
import ru.saidgadjiev.aboutme.service.BlogService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/comment")
public class CommentController {

    @Autowired
    private BlogService blogService;

    @GetMapping(value = "/{id}/comments")
    public ResponseEntity<Page<ObjectNode>> getCommentsByPost(
            @PathVariable("id") Integer id,
            @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        List<Comment> comments = blogService.getCommentsList(id, page.getPageSize(), page.getOffset());
        long total = blogService.commentCountOff(id);
        List<ObjectNode> content = new ArrayList<>();

        for (Comment comment: comments) {
            content.add(new CommentJsonBuilder()
            .id(comment.getId())
            .content(comment.getContent())
            .createdDate(comment.getCreatedDate())
            .nickName(comment.getUser().getNickName())
            .build());
        }

        return ResponseEntity.ok(new PageImpl<>(content, page, total));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/{id}/create")
    public ResponseEntity createCommentOfPost(
            @PathVariable("id") Integer id,
            @RequestBody @Valid CommentRequest commentRequest,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        blogService.createCommentOfPost(id, commentRequest);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("@authorization.canEditComment(#id)")
    @PostMapping(value = "/update/{id}")
    public ResponseEntity<ObjectNode> updateComment(
            @PathVariable("id") Integer id,
            @RequestBody CommentRequest commentRequest
    ) throws SQLException {
        blogService.updateComment(id, commentRequest);

        return ResponseEntity.ok(new CommentJsonBuilder().content(commentRequest.getContent()).build());
    }

    @PreAuthorize("@authorization.canDeleteComment(#id)")
    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Integer id) throws SQLException {
        int deleted = blogService.deleteCommentById(id);

        if (deleted == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
