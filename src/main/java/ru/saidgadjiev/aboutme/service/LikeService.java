package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.LikeDao;
import ru.saidgadjiev.aboutme.dao.PostDao;
import ru.saidgadjiev.aboutme.dao.UserProfileDao;
import ru.saidgadjiev.aboutme.domain.Like;

import java.sql.SQLException;

@Service
public class LikeService {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private LikeDao likeDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private UserProfileDao userProfileDao;

    public void postLike(Integer postId) throws SQLException {
        UserDetails authorizedUser = securityService.findLoggedInUser();
        Like like = new Like();

        like.setPost(postDao.getById(postId));

        like.setUser(userProfileDao.getByUserName(authorizedUser.getUsername()));

        likeDao.create(like);
    }

    public void postDislike(Integer postId) throws SQLException {
        UserDetails authorizedUser = securityService.findLoggedInUser();

        likeDao.delete(postId, authorizedUser.getUsername());
    }

    public long postLikesCount(int postId) throws SQLException {
        return likeDao.countOffByPostId(postId);
    }
}
