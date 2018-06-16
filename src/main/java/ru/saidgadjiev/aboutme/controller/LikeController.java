package ru.saidgadjiev.aboutme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.saidgadjiev.aboutme.domain.UserProfile;
import ru.saidgadjiev.aboutme.model.LikeDetails;
import ru.saidgadjiev.aboutme.model.ResponseMessage;
import ru.saidgadjiev.aboutme.service.LikeService;
import ru.saidgadjiev.aboutme.service.SecurityService;

import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/like/post", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage<LikeDetails>> like(@RequestBody LikeDetails likeDetails) throws SQLException {
        UserDetails authorizedUser = securityService.findLoggedInUser();

        likeDetails.setUser(authorizedUser.getUsername());
        likeService.create(likeDetails);
        likeDetails.setLikesCount((int) likeService.postLikes(likeDetails.getPostId()));
        likeDetails.setLiked(true);

        return ResponseEntity.ok(new ResponseMessage<>("", likeDetails));
    }

    @RequestMapping(value = "/dislike/post", method = RequestMethod.POST)
    public ResponseEntity<ResponseMessage<LikeDetails>> dislike(@RequestBody LikeDetails likeDetails) throws SQLException {
        UserDetails authorizedUser = securityService.findLoggedInUser();

        likeDetails.setUser(authorizedUser.getUsername());
        likeService.remove(likeDetails);
        likeDetails.setLikesCount((int) likeService.postLikes(likeDetails.getPostId()));
        likeDetails.setLiked(false);

        return ResponseEntity.ok(new ResponseMessage<>("", likeDetails));
    }
}
