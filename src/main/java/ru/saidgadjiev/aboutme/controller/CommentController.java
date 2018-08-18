package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.Comment;
import ru.saidgadjiev.aboutme.json.CommentJsonBuilder;
import ru.saidgadjiev.aboutme.json.JsonBuilder;
import ru.saidgadjiev.aboutme.model.CommentDetails;
import ru.saidgadjiev.aboutme.model.JsonViews;
import ru.saidgadjiev.aboutme.service.BlogService;
import ru.saidgadjiev.aboutme.utils.DTOUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/comment")
public class CommentController {

    private static final String COMMENTS_TOPIC = "/topic/comments";

    @Autowired
    private BlogService blogService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping(value = "/{id}/comments")
    public ResponseEntity<Page<CommentDetails>> getCommentsByPost(
            @PathVariable("id") Integer id,
            @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        Page<Comment> result = blogService.getCommentsList(id, page);
        List<CommentDetails> converted = DTOUtils.convert(result.getContent(), CommentDetails.class);

        return ResponseEntity.ok(new PageImpl<>(converted, page, result.getTotalElements()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/{id}/create")
    public ResponseEntity<CommentDetails> createCommentOfPost(
            @PathVariable("id") Integer id,
            @JsonView(JsonViews.Rest.class) @RequestBody @Valid CommentDetails commentDetails,
            BindingResult bindingResult
    ) throws SQLException, IOException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Comment comment = blogService.createCommentOfPost(id, commentDetails);
        CommentDetails result = DTOUtils.convert(comment, CommentDetails.class);

        ObjectNode webSocketResponse = new JsonBuilder()
                .add("status", HttpStatus.CREATED.value())
                .add(
                        "content",
                        new JsonBuilder()
                                .add("commentId", result.getId())
                                .add("content", result.getContent())
                                .add("createdDate", result.getCreatedDate())
                                .add("nickName", result.getNickName())
                                .add("postId", id)
                                .add("commentsCount", blogService.commentCountOff(id))
                                .build()
                )
                .build();

        messagingTemplate.convertAndSend(COMMENTS_TOPIC, webSocketResponse);

        return ResponseEntity.ok(result);
    }

    @PreAuthorize("@authorization.canEditComment(#id)")
    @PatchMapping(value = "/{postId}/update/{id}")
    public ResponseEntity<ObjectNode> updateComment(
            @PathVariable("postId") Integer postId,
            @PathVariable("id") Integer id,
            @JsonView(JsonViews.Rest.class) @RequestBody CommentDetails commentDetails
    ) throws SQLException {
        Comment comment = blogService.updateComment(id, commentDetails);

        if (comment == null) {
            return ResponseEntity.notFound().build();
        }

        ObjectNode response = new CommentJsonBuilder()
                .content(comment.getContent())
                .build();

        ObjectNode webSocketResponse = new JsonBuilder()
                .add("status", HttpStatus.PARTIAL_CONTENT.value())
                .add(
                        "content",
                        new JsonBuilder()
                                .add("commentId", id)
                                .add("postId", postId)
                                .add("content", comment.getContent())
                                .build()
                )
                .build();

        messagingTemplate.convertAndSend(COMMENTS_TOPIC, webSocketResponse);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("@authorization.canDeleteComment(#id)")
    @DeleteMapping(value = "/{postId}/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("postId") Integer postId, @PathVariable("id") Integer id) throws SQLException {
        int deleted = blogService.deleteCommentById(id);

        if (deleted == 0) {
            return ResponseEntity.notFound().build();
        }

        ObjectNode webSocketResponse = new JsonBuilder()
                .add("status", HttpStatus.NO_CONTENT.value())
                .add(
                        "content",
                        new JsonBuilder()
                                .add("commentId", id)
                                .add("postId", postId)
                                .add("commentsCount", blogService.commentCountOff(postId))
                                .build()
                ).build();

        messagingTemplate.convertAndSend(COMMENTS_TOPIC, webSocketResponse);

        return ResponseEntity.ok().build();
    }
}
