package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.annotation.JsonView;
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
import ru.saidgadjiev.aboutme.model.JsonViews;
import ru.saidgadjiev.aboutme.model.PostDetails;
import ru.saidgadjiev.aboutme.service.BlogService;
import ru.saidgadjiev.aboutme.utils.DTOUtils;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            @JsonView(JsonViews.Rest.class) @RequestBody @Valid PostDetails postDetails,
            BindingResult bindingResult
    ) throws SQLException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Post post = blogService.createPostOfCategory(categoryId, postDetails);
        PostDetails response = DTOUtils.convert(post, PostDetails.class);

        response.setLiked(blogService.isLikedByCurrentUser(post));

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/update/{id}")
    public ResponseEntity<ObjectNode> update(
            @PathVariable("id") Integer id,
            @JsonView(JsonViews.Rest.class) @RequestBody @Valid PostDetails postDetails,
            BindingResult errResult
    ) throws SQLException {
        if (errResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Post post = blogService.updatePost(id, postDetails);

        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        ObjectNode response = new PostJsonBuilder()
                .content(post.getContent())
                .title(post.getTitle())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}/posts")
    public ResponseEntity<Page<PostDetails>> getPostsOfCategory(
            @PathVariable("id") Integer categoryId,
            @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        Page<Post> result = blogService.getPostsList(categoryId, page);
        Map<Integer, PostDetails> response = DTOUtils.convert(result.getContent(), PostDetails.class)
                .stream()
                .collect(Collectors.toMap(PostDetails::getId, Function.identity()));

        for (Post post : result.getContent()) {
            response.get(post.getId()).setLiked(blogService.isLikedByCurrentUser(post));
        }

        return ResponseEntity.ok(new PageImpl<>(new ArrayList<>(response.values()), page, result.getTotalElements()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDetails> getPost(@PathVariable("id") Integer id) throws SQLException {
        Post post = blogService.getPostById(id);
        PostDetails postDetails = DTOUtils.convert(post, PostDetails.class);

        postDetails.setLiked(blogService.isLikedByCurrentUser(post));

        return ResponseEntity.ok(postDetails);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Integer id) throws SQLException {
        int deleted = blogService.deletePostById(id);

        if (deleted == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
