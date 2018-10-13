package ru.saidgadjiev.aboutme.service;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.CategoryDao;
import ru.saidgadjiev.aboutme.dao.CommentDao;
import ru.saidgadjiev.aboutme.dao.PostDao;
import ru.saidgadjiev.aboutme.dao.UserProfileDao;
import ru.saidgadjiev.aboutme.domain.Category;
import ru.saidgadjiev.aboutme.domain.Comment;
import ru.saidgadjiev.aboutme.domain.Post;
import ru.saidgadjiev.aboutme.model.CategoryDetails;
import ru.saidgadjiev.aboutme.model.CommentDetails;
import ru.saidgadjiev.aboutme.model.PostDetails;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by said on 08.03.2018.
 */
@Service
public class BlogService {

    private PostDao postDao;

    private CommentDao commentDao;

    private CategoryDao categoryDao;

    private SecurityService securityService;

    private UserProfileDao userProfileDao;

    @Autowired
    public BlogService(PostDao postDao,
                       CommentDao commentDao,
                       CategoryDao categoryDao,
                       SecurityService securityService,
                       UserProfileDao userProfileDao) {
        this.postDao = postDao;
        this.commentDao = commentDao;
        this.categoryDao = categoryDao;
        this.securityService = securityService;
        this.userProfileDao = userProfileDao;
    }

    public Comment createCommentOfPost(Integer postId, CommentDetails commentDetails) throws SQLException {
        Comment comment = new Comment();

        comment.setContent(commentDetails.getContent());
        comment.setPost(postDao.getById(postId));
        UserDetails authorizedUser = securityService.findLoggedInUser();

        comment.setUser(userProfileDao.getByUserName(authorizedUser.getUsername()));

        commentDao.create(comment);

        return comment;
    }

    public Post createPostOfCategory(Integer categoryId, PostDetails postDetails) throws SQLException {
        Post post = new Post();

        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        post.setCategory(categoryDao.getById(categoryId));

        postDao.create(post);

        return post;
    }

    public Page<Post> getPostsList(Integer categoryId, Pageable pageable) throws SQLException {
        List<Post> posts = postDao.getPosts(categoryId, pageable.getPageSize(), pageable.getOffset());
        long total = postDao.countOffPostsByCategoryId(categoryId);

        return new PageImpl<>(posts, pageable, total);
    }

    public Page<Comment> getCommentsList(Integer postId, Pageable pageable) throws SQLException {
         List<Comment> comments = commentDao.getByPostId(postId, pageable.getPageSize(), pageable.getOffset());
         long total = commentDao.countOffByPostId(postId);

         return new PageImpl<>(comments, pageable, total);
    }

    public Post getPostById(Integer id) throws SQLException {
        return postDao.getById(id);
    }

    public Post updatePost(Integer id, PostDetails postDetails) throws SQLException {
        Post post = postDao.getById(id);

        if (post == null) {
            return null;
        }
        post.setContent(postDetails.getContent());
        post.setTitle(postDetails.getTitle());

        postDao.update(post);

        return post;
    }

    @Nullable
    public Comment updateComment(Integer id, CommentDetails commentDetails) throws SQLException {
        Comment comment = commentDao.getById(id);

        if (comment == null) {
            return null;
        }
        comment.setContent(commentDetails.getContent());

        commentDao.update(comment);

        return comment;
    }

    public Page<Category> getCategories(Pageable pageable) throws SQLException {
        List<Category> categories = categoryDao.getCategories(pageable.getPageSize(), pageable.getOffset());
        long total = categoryDao.countOff();

        return new PageImpl<>(categories, pageable, total);
    }

    public Category createCategory(CategoryDetails categoryDetails) throws SQLException {
        Category category = new Category();

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        categoryDao.create(category);

        return category;
    }

    @Nullable
    public Category updateCategory(Integer id, CategoryDetails categoryDetails) throws SQLException {
        Category category = categoryDao.getById(id);

        if (category == null) {
            return null;
        }

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        categoryDao.update(category);

        return category;
    }

    public Category getCategoryById(Integer id) throws SQLException {
        return categoryDao.getById(id);
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

    public boolean isLikedByCurrentUser(Post post) {
        UserDetails userDetails = securityService.findLoggedInUser();

        if (userDetails != null) {
            List<String> likedUsers = post.getLikes()
                    .stream()
                    .map(like -> like.getUser().getUsername())
                    .collect(Collectors.toList());

            return likedUsers.contains(userDetails.getUsername());
        }

        return false;
    }

    public long commentCountOff(Integer postId) throws SQLException {
        return commentDao.countOffByPostId(postId);
    }
}
