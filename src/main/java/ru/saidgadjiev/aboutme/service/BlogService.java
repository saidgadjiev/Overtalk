package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.CommentDao;
import ru.saidgadjiev.aboutme.dao.PostDao;
import ru.saidgadjiev.aboutme.domain.Comment;
import ru.saidgadjiev.aboutme.domain.Post;
import ru.saidgadjiev.aboutme.model.CommentDetails;
import ru.saidgadjiev.aboutme.model.PostDetails;
import ru.saidgadjiev.aboutme.utils.DTOUtils;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Created by said on 08.03.2018.
 */
@Service
public class BlogService {

    private PostDao postDao;

    private CommentDao commentDao;

    private SecurityService securityService;

    @Autowired
    public BlogService(PostDao postDao, CommentDao commentDao, SecurityService securityService) {
        this.postDao = postDao;
        this.commentDao = commentDao;
        this.securityService = securityService;
    }

    public void createCommentOfPost(Integer id, CommentDetails details) throws SQLException {
        UserDetails authorizedUser = securityService.findLoggedInUser();

        details.setUser(authorizedUser.getUsername());
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

        List<PostDetails> postDetailsList = DTOUtils.convert(posts, PostDetails.class);
        UserDetails userDetails = securityService.findLoggedInUser();

        if (userDetails != null) {
            for (PostDetails postDetails: postDetailsList) {
                postDetails.setLiked(postDetails.getLikeUsers().contains(userDetails.getUsername()));
            }
        }

        return new PageImpl<>(postDetailsList, page, totalCount);
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

    public int updatePost(PostDetails postDetails) throws SQLException {
        Post post = DTOUtils.convert(postDetails, Post.class);

        return postDao.update(post);
    }

    public int updateComment(CommentDetails commentDetails) throws SQLException {
        Comment comment = DTOUtils.convert(commentDetails, Comment.class);

        return commentDao.update(comment);
    }
}
