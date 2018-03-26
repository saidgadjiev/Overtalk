package ru.saidgadjiev.overtalk.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.overtalk.application.dao.CommentDao;
import ru.saidgadjiev.overtalk.application.dao.PostDao;
import ru.saidgadjiev.overtalk.application.domain.Comment;
import ru.saidgadjiev.overtalk.application.domain.Post;
import ru.saidgadjiev.overtalk.application.model.CommentDetails;
import ru.saidgadjiev.overtalk.application.model.PostDetails;
import ru.saidgadjiev.overtalk.application.utils.DTOUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by said on 08.03.2018.
 */
@Service
public class BlogService {

    private PostDao postDao;

    private CommentDao commentDao;

    @Autowired
    public BlogService(PostDao postDao, CommentDao commentDao) {
        this.postDao = postDao;
        this.commentDao = commentDao;
    }

    public void createCommentOfPost(Integer id, CommentDetails details) throws SQLException {
        Comment comment = DTOUtils.convert(details, Comment.class);
        Post post = postDao.getById(id);

        comment.setPost(post);
        commentDao.create(comment);
    }

    public void createPost(PostDetails postDetails) throws SQLException {
        Post post = DTOUtils.convert(postDetails, Post.class);

        postDao.create(post);
    }

    public Page<PostDetails> getAllPosts(Pageable page) throws SQLException {
        long totalCount = postDao.countOff();
        List<Post> posts = postDao.getAll(page.getPageSize(), page.getOffset());

        return new PageImpl<>(DTOUtils.convert(posts, PostDetails.class), page, totalCount);
    }

    public Page<CommentDetails> getCommentsByPostId(Integer id, Pageable page) throws SQLException {
        long total = commentDao.countOffByPostId(id);
        List<Comment> comments = commentDao.getByPostId(id, page.getPageSize(), (int) page.getOffset());

        return new PageImpl<>(DTOUtils.convert(comments, CommentDetails.class), page, total);
    }

    public PostDetails getPostById(Integer id) throws SQLException {
        Post post = postDao.getById(id);

        return DTOUtils.convert(post, PostDetails.class);
    }
}
