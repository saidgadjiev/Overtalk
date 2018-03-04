package ru.saidgadjiev.overtalk.application.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.overtalk.application.model_service.CommentService;
import ru.saidgadjiev.overtalk.application.model_service.PostService;
import ru.saidgadjiev.overtalk.application.model.dao.Comment;
import ru.saidgadjiev.overtalk.application.model.dao.Post;
import ru.saidgadjiev.overtalk.application.model.ResponseMessage;
import ru.saidgadjiev.overtalk.application.model.web.PostDetails;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by said on 12.02.2018.
 */
@RestController
@RequestMapping(value = "/api/post")
public class PostController {

    private final PostService postService;

    private final CommentService commentService;

    private static final Logger LOGGER = Logger.getLogger(PostController.class);

    @Autowired
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> createPost(@RequestBody @Valid Post post, BindingResult errResult) throws SQLException {
        LOGGER.debug("createPost(Post: " + post.toString() + ")");

        postService.create(post);

        return ResponseEntity.ok(new ResponseMessage(200, "post.created"));
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<PostDetails>> getAllPosts() throws SQLException {
        LOGGER.debug("getAllPosts()");
        List<PostDetails> posts = postService.getAll();

        return ResponseEntity.ok(posts);
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.GET)
    public ResponseEntity<PostDetails> getCommentsByPost(@PathVariable("id") Integer id) throws SQLException {
        LOGGER.debug("getCommentsByPost()");
        PostDetails post = postService.getById(id);

        return ResponseEntity.ok(post);
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage> createCommentOfPost(@PathVariable("id") Integer id, @RequestBody @Valid Comment comment) throws SQLException {
        PostDetails post = postService.getById(id);

        post.setId(id);
        comment.setPost(post);
        commentService.create(comment);

        return ResponseEntity.ok(new ResponseMessage(200, "comment.created"));
    }
}
