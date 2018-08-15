package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.LikeDao;
import ru.saidgadjiev.aboutme.domain.Like;
import ru.saidgadjiev.aboutme.domain.Post;
import ru.saidgadjiev.aboutme.domain.UserProfile2;

import java.sql.SQLException;

@Service
public class LikeService {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private LikeDao likeDao;

    public Like postLike(Integer postId) throws SQLException {
        UserDetails authorizedUser = securityService.findLoggedInUser();
        Like like = new Like();
        Post post = new Post();

        post.setId(postId);
        like.setPost(post);

        UserProfile2 userProfile = new UserProfile2();

        userProfile.setUserName(authorizedUser.getUsername());
        like.setUser(userProfile);

        likeDao.create(like);

        return like;
    }

    public Like postDislike(Integer postId) throws SQLException {
        UserDetails authorizedUser = securityService.findLoggedInUser();
        Like like = new Like();
        Post post = new Post();

        post.setId(postId);
        like.setPost(post);

        UserProfile2 userProfile = new UserProfile2();

        userProfile.setUserName(authorizedUser.getUsername());
        like.setUser(userProfile);

        likeDao.deletePostLike(like);

        return like;
    }

    public long postLikesCount(int postId) throws SQLException {
        return likeDao.postLikes(postId);
    }
}
