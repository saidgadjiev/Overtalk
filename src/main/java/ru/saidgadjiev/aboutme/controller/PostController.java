package ru.saidgadjiev.aboutme.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private static final Logger LOGGER = Logger.getLogger(PostController.class);

    @Autowired
    private BlogService blogService;

    @RequestMapping(value = "/{id}/create", method = RequestMethod.POST)
    public ResponseEntity createPostOfCategory(
            @PathVariable("id") Integer categoryId,
            @RequestBody PostDetails postDetails) throws SQLException {
        LOGGER.debug("createPost(Post: " + postDetails.toString() + ")");

        blogService.createPostOfCategory(categoryId, postDetails);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<PostDetails> update(@RequestBody @Valid PostDetails postDetails, BindingResult errResult) throws SQLException {
        LOGGER.debug("update(Post: " + postDetails.toString() + ")");

        blogService.updatePost(postDetails);

        return ResponseEntity.ok(postDetails);
    }

    @RequestMapping(value = "/{id}/posts", method = RequestMethod.GET)
    public ResponseEntity<Page<PostDetails>> getPostsOfCategory(
            @PathVariable("id") Integer categoryId,
            @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        LOGGER.debug("getPostsByCategoryId()");
        Page<PostDetails> posts = blogService.getPostsByCategoryId(categoryId, page);

        return ResponseEntity.ok(posts);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<PostDetails> getPost(@PathVariable("id") Integer id) throws SQLException {
        LOGGER.debug("getPostId()" + id);
        PostDetails post = blogService.getPostById(id);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }
}
