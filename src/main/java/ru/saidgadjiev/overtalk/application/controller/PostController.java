package ru.saidgadjiev.overtalk.application.controller;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.overtalk.application.model.ResponseMessage;
import ru.saidgadjiev.overtalk.application.model.CommentDetails;
import ru.saidgadjiev.overtalk.application.model.PostDetails;
import ru.saidgadjiev.overtalk.application.service.BlogService;

import javax.validation.Valid;
import java.sql.SQLException;

/**
 * Created by said on 12.02.2018.
 */
@RestController
@RequestMapping(value = "/api/post")
public class PostController {

    private BlogService blogService;

    private static final Logger LOGGER = Logger.getLogger(PostController.class);

    public PostController(BlogService blogService) {
        this.blogService = blogService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity createPost(@RequestBody @Valid PostDetails postDetails, BindingResult errResult) throws SQLException {
        LOGGER.debug("createPost(Post: " + postDetails.toString() + ")");

        blogService.createPost(postDetails);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Page<PostDetails>> getAllPosts(
            @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.DESC) Pageable page) throws SQLException {
        LOGGER.debug("getAllPosts()");
        Page<PostDetails> posts = blogService.getAllPosts(page);

        return ResponseEntity.ok(posts);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<PostDetails> getPost(@PathVariable("id") Integer id) throws SQLException {
        LOGGER.debug("getPost()" + id);
        PostDetails post = blogService.getPostById(id);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.GET)
    public ResponseEntity<Page<CommentDetails>> getCommentsByPost(
            @PathVariable("id") Integer id,
            @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable page
    ) throws SQLException {
        LOGGER.debug("getCommentsByPost(): " + id + ", " + page);
        Page<CommentDetails> commentDetails = blogService.getCommentsByPostId(id, page);

        return ResponseEntity.ok(commentDetails);
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> createCommentOfPost(@PathVariable("id") Integer id, @RequestBody @Valid CommentDetails commentDetails) throws SQLException {
        blogService.createCommentOfPost(id, commentDetails);

        return ResponseEntity.ok().build();
    }
}
