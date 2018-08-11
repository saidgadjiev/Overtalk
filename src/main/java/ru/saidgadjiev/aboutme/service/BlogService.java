package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.CategoryDao;
import ru.saidgadjiev.aboutme.dao.CommentDao;
import ru.saidgadjiev.aboutme.dao.PostDao;
import ru.saidgadjiev.aboutme.domain.Category;
import ru.saidgadjiev.aboutme.domain.Comment;
import ru.saidgadjiev.aboutme.domain.Post;
import ru.saidgadjiev.aboutme.model.CategoryDetails;
import ru.saidgadjiev.aboutme.model.CommentDetails;
import ru.saidgadjiev.aboutme.model.PostDetails;
import ru.saidgadjiev.aboutme.utils.DTOUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by said on 08.03.2018.
 */
@Service
public class BlogService {

    private PostDao postDao;

    private CommentDao commentDao;

    private CategoryDao categoryDao;

    private SecurityService securityService;

    @Autowired
    public BlogService(PostDao postDao, CommentDao commentDao, CategoryDao categoryDao, SecurityService securityService) {
        this.postDao = postDao;
        this.commentDao = commentDao;
        this.categoryDao = categoryDao;
        this.securityService = securityService;
    }

    public void createCommentOfPost(Integer postId, CommentDetails details) throws SQLException {
        UserDetails authorizedUser = securityService.findLoggedInUser();

        details.setUser(authorizedUser.getUsername());
        Comment comment = DTOUtils.convert(details, Comment.class);
        Post post = new Post();

        post.setId(postId);
        comment.setPost(post);
        commentDao.create(comment);
    }

    public void createPostOfCategory(Integer categoryId, PostDetails postDetails) throws SQLException {
        UserDetails authorizedUser = securityService.findLoggedInUser();

        postDetails.setUserName(authorizedUser.getUsername());
        Post post = DTOUtils.convert(postDetails, Post.class);
        Category category = new Category();

        category.setId(categoryId);
        post.setCategory(category);
        postDao.create(post);
    }

    public Page<PostDetails> getPostsByCategoryId(Integer categoryId, Pageable page) throws SQLException {
        long totalCount = postDao.countOffPostsByCategoryId(categoryId);
        List<Post> posts = postDao.getPosts(categoryId, page.getPageSize(), page.getOffset());

        List<PostDetails> postDetailsList = DTOUtils.convert(posts, PostDetails.class);
        UserDetails userDetails = securityService.findLoggedInUser();

        if (userDetails != null) {
            for (PostDetails postDetails : postDetailsList) {
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
        PostDetails postDetails = DTOUtils.convert(post, PostDetails.class);
        UserDetails userDetails = securityService.findLoggedInUser();

        if (userDetails != null) {
            postDetails.setLiked(postDetails.getLikeUsers().contains(userDetails.getUsername()));
        }

        return postDetails;
    }

    public int updatePost(PostDetails postDetails) throws SQLException {
        Post post = DTOUtils.convert(postDetails, Post.class);

        return postDao.update(post);
    }

    public int updateComment(CommentDetails commentDetails) throws SQLException {
        Comment comment = DTOUtils.convert(commentDetails, Comment.class);

        return commentDao.update(comment);
    }

    public Page<CategoryDetails> getCategories(Pageable page) throws SQLException {
        long totalCount = categoryDao.countOff();
        List<Category> categories = categoryDao.getCategories(page.getPageSize(), page.getOffset());
        List<CategoryDetails> postDetailsList = DTOUtils.convert(categories, CategoryDetails.class);

        return new PageImpl<>(postDetailsList, page, totalCount);
    }

    public void createCategory(CategoryDetails categoryDetails) throws SQLException {
        Category category = DTOUtils.convert(categoryDetails, Category.class);

        categoryDao.create(category);
    }

    public int updateCategory(Integer id, CategoryDetails categoryDetails) throws SQLException {
        Category category = DTOUtils.convert(categoryDetails, Category.class);

        return categoryDao.update(id, category);
    }

    public CategoryDetails getCategoryById(Integer id) throws SQLException {
        return DTOUtils.convert(categoryDao.getById(id), CategoryDetails.class);
    }

    public int deleteCommentById(Integer id) throws SQLException {
        return commentDao.deleteById(id);
    }

    public int deletePostById(Integer id) throws SQLException {
        return postDao.deleteById(id);
    }

    public int deleteCategoryById(Integer id) throws SQLException {
        return categoryDao.deleteById(id);
    }
}
