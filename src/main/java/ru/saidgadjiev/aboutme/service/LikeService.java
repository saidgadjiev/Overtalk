package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.LikeDao;
import ru.saidgadjiev.aboutme.domain.Like;
import ru.saidgadjiev.aboutme.domain.Post;
import ru.saidgadjiev.aboutme.domain.UserProfile;
import ru.saidgadjiev.aboutme.domain.UserProfile2;
import ru.saidgadjiev.aboutme.model.LikeDetails;
import ru.saidgadjiev.aboutme.utils.DTOUtils;

import java.sql.SQLException;

@Service
public class LikeService {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private LikeDao likeDao;

    public LikeDetails postLike(Integer postId) throws SQLException {
        UserDetails authorizedUser = securityService.findLoggedInUser();
        Like like = new Like();
        Post post = new Post();

        post.setId(postId);
        like.setPost(post);

        UserProfile2 userProfile = new UserProfile2();

        userProfile.setUserName(authorizedUser.getUsername());
        like.setUser(userProfile);

        likeDao.create(like);

        LikeDetails likeDetails = new LikeDetails();

        likeDetails.setPostId(postId);
        likeDetails.setLikesCount((int) likeDao.postLikes(postId));
        likeDetails.setLiked(true);

        return likeDetails;
    }

    public LikeDetails postDislike(Integer postId) throws SQLException {
        UserDetails authorizedUser = securityService.findLoggedInUser();
        Like like = new Like();
        Post post = new Post();

        post.setId(postId);
        like.setPost(post);

        UserProfile2 userProfile = new UserProfile2();

        userProfile.setUserName(authorizedUser.getUsername());
        like.setUser(userProfile);

        likeDao.deletePostLike(like);

        LikeDetails likeDetails = new LikeDetails();

        likeDetails.setPostId(postId);
        likeDetails.setLikesCount((int) likeDao.postLikes(postId));
        likeDetails.setLiked(false);

        return likeDetails;
    }
}
