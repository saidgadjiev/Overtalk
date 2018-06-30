package ru.saidgadjiev.aboutme.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.model.PostDetails;
import ru.saidgadjiev.aboutme.model.ResponseMessage;
import ru.saidgadjiev.aboutme.service.BlogService;
import ru.saidgadjiev.aboutme.service.SecurityService;

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

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity createPost(@RequestBody @Valid PostDetails postDetails, BindingResult errResult) throws SQLException {
        UserDetails authorizedUser = securityService.findLoggedInUser();

        postDetails.setUserName(authorizedUser.getUsername());
        LOGGER.debug("createPost(Post: " + postDetails.toString() + ")");

        blogService.createPost(postDetails);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage<PostDetails>> updatePost(@RequestBody @Valid PostDetails postDetails, BindingResult errResult) throws SQLException {
        UserDetails authorizedUser = securityService.findLoggedInUser();

        if (authorizedUser.getUsername().equals(postDetails.getUserName())) {
            return ResponseEntity.notFound().build();
        }
        postDetails.setUserName(authorizedUser.getUsername());
        LOGGER.debug("createPost(Post: " + postDetails.toString() + ")");

        blogService.updatePost(postDetails);

        return ResponseEntity.ok(new ResponseMessage<>("", postDetails));
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
        LOGGER.debug("getPostId()" + id);
        PostDetails post = blogService.getPostById(id);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }
}
