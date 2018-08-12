package ru.saidgadjiev.aboutme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.model.PostDetails;
import ru.saidgadjiev.aboutme.service.BlogService;

import javax.validation.Valid;
import java.sql.SQLException;

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
            @RequestBody @Valid PostDetails postDetails,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        blogService.createPostOfCategory(categoryId, postDetails);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/update/{id}")
    public ResponseEntity<PostDetails> update(
            @PathVariable("id") Integer id,
            @RequestBody @Valid PostDetails postDetails, BindingResult errResult
    ) throws SQLException {
        if (errResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        blogService.updatePost(id, postDetails);

        return ResponseEntity.ok(postDetails);
    }

    @GetMapping(value = "/{id}/posts")
    public ResponseEntity<Page<PostDetails>> getPostsOfCategory(
            @PathVariable("id") Integer categoryId,
            @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        Page<PostDetails> posts = blogService.getPostsByCategoryId(categoryId, page);

        return ResponseEntity.ok(posts);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDetails> getPost(@PathVariable("id") Integer id) throws SQLException {
        PostDetails post = blogService.getPostById(id);

        return ResponseEntity.ok(post);
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
}
