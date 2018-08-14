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
import ru.saidgadjiev.aboutme.domain.Post;
import ru.saidgadjiev.aboutme.json.PostJsonBuilder;
import ru.saidgadjiev.aboutme.model.PostRequest;
import ru.saidgadjiev.aboutme.service.BlogService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by said on 12.02.2018.
 */
@RestController
@RequestMapping(value = "/api/post")
public class PostController {

    @Autowired
    private BlogService blogService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{id}/create")
    public ResponseEntity createPostOfCategory(
            @PathVariable("id") Integer categoryId,
            @RequestBody @Valid PostRequest postRequest,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        blogService.createPostOfCategory(categoryId, postRequest);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/update/{id}")
    public ResponseEntity<ObjectNode> update(
            @PathVariable("id") Integer id,
            @RequestBody @Valid PostRequest postRequest, BindingResult errResult
    ) throws SQLException {
        if (errResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        int updated = blogService.updatePost(id, postRequest);

        if (updated == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new PostJsonBuilder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .build());
    }

    @GetMapping(value = "/{id}/posts")
    public ResponseEntity<Page<ObjectNode>> getPostsOfCategory(
            @PathVariable("id") Integer categoryId,
            @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        List<Post> posts = blogService.getPostsList(categoryId, page.getPageSize(), page.getOffset());
        long total = blogService.postCountOff(categoryId);
        List<ObjectNode> content = new ArrayList<>();

        for (Post post : posts) {
            content.add(toJson(post));
        }

        return ResponseEntity.ok(new PageImpl<>(content, page, total));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ObjectNode> getPost(@PathVariable("id") Integer id) throws SQLException {
        Post post = blogService.getPostById(id);

        return ResponseEntity.ok(toJson(post));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Integer id) throws SQLException {
        int deleted = blogService.deletePostById(id);

        if (deleted == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    private ObjectNode toJson(Post post) {
        return new PostJsonBuilder()
                .id(post.getId())
                .content(post.getContent())
                .title(post.getTitle())
                .createdDate(post.getCreatedDate())
                .commentsCount(post.getComments().size())
                .likesCount(post.getLikes().size())
                .liked(blogService.isLikedByCurrentUser(post))
                .build();
    }
}
