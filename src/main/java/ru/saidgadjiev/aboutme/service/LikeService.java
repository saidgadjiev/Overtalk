package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.LikeDao;
import ru.saidgadjiev.aboutme.domain.Like;
import ru.saidgadjiev.aboutme.model.LikeDetails;
import ru.saidgadjiev.aboutme.utils.DTOUtils;

import java.sql.SQLException;

@Service
public class LikeService {

    @Autowired
    private LikeDao likeDao;

    public void create(LikeDetails likeDetails) throws SQLException {
        Like like = DTOUtils.convert(likeDetails, Like.class);

        likeDao.create(like);
    }

    public void remove(LikeDetails likeDetails) throws SQLException {
        Like like = DTOUtils.convert(likeDetails, Like.class);

        likeDao.delete(like);
    }

    public long postLikes(Integer postId) throws SQLException {
        return likeDao.postLikes(postId);
    }
}
