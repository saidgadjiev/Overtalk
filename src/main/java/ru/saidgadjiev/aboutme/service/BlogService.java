package ru.saidgadjiev.aboutme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.aboutme.dao.CategoryDao;
import ru.saidgadjiev.aboutme.dao.CommentDao;
import ru.saidgadjiev.aboutme.dao.PostDao;
import ru.saidgadjiev.aboutme.domain.Category;
import ru.saidgadjiev.aboutme.domain.Comment;
import ru.saidgadjiev.aboutme.domain.Post;
import ru.saidgadjiev.aboutme.domain.UserProfile2;
import ru.saidgadjiev.aboutme.model.CategoryRequest;
import ru.saidgadjiev.aboutme.model.CommentRequest;
import ru.saidgadjiev.aboutme.model.PostRequest;

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

    @Autowired
    public BlogService(PostDao postDao,
                       CommentDao commentDao,
                       CategoryDao categoryDao,
                       SecurityService securityService) {
        this.postDao = postDao;
        this.commentDao = commentDao;
        this.categoryDao = categoryDao;
        this.securityService = securityService;
    }

    public void createCommentOfPost(Integer postId, CommentRequest commentRequest) throws SQLException {
        Comment comment = new Comment();

        comment.setContent(commentRequest.getContent());
        Post post = new Post();

        post.setId(postId);

        UserDetails authorizedUser = securityService.findLoggedInUser();
        UserProfile2 userProfile = new UserProfile2();

        userProfile.setUserName(authorizedUser.getUsername());

        comment.setUser(userProfile);
        comment.setPost(post);

        commentDao.create(comment);
    }

    public void createPostOfCategory(Integer categoryId, PostRequest postRequest) throws SQLException {
        Post post = new Post();

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        Category category = new Category();

        category.setId(categoryId);
        post.setCategory(category);

        postDao.create(post);
    }

    public List<Post> getPostsList(Integer categoryId, int limit, long offset) throws SQLException {
        return postDao.getPosts(categoryId, limit, offset);
    }

    public List<Comment> getCommentsList(Integer postId, int limit, long offset) throws SQLException {
        return commentDao.getByPostId(postId, limit, offset);
    }

    public Post getPostById(Integer id) throws SQLException {
        return postDao.getById(id);
    }

    public int updatePost(Integer id, PostRequest postRequest) throws SQLException {
        return postDao.update(id, postRequest.getTitle(), postRequest.getContent());
    }

    public int updateComment(Integer id, CommentRequest commentRequest) throws SQLException {
        Comment comment = new Comment();

        comment.setContent(commentRequest.getContent());
        Post post = new Post();

        post.setId(id);
        comment.setPost(post);

        return commentDao.update(id, commentRequest.getContent());
    }

    public List<Category> getCategoriesList(int limit, long offset) throws SQLException {
        return categoryDao.getCategories(limit, offset);
    }

    public Category createCategory(CategoryRequest categoryRequest) throws SQLException {
        Category category = new Category();

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        categoryDao.create(category);

        return category;
    }

    public int updateCategory(Integer id, CategoryRequest categoryRequest) throws SQLException {
        Category category = new Category();

        category.setId(id);
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        return categoryDao.update(category);
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

    public long categoryCountOff() throws SQLException {
        return categoryDao.countOff();
    }

    public long commentCountOff(int postId) throws SQLException {
        return commentDao.countOffByPostId(postId);
    }

    public long postCountOff(int categoryId) throws SQLException {
        return postDao.countOffPostsByCategoryId(categoryId);
    }

    public boolean isLikedByCurrentUser(Post post) {
        UserDetails userDetails = securityService.findLoggedInUser();

        if (userDetails != null) {
            List<String> likedUsers = post.getLikes()
                    .stream()
                    .map(like -> like.getUser().getUserName())
                    .collect(Collectors.toList());

            return likedUsers.contains(userDetails.getUsername());
        }

        return false;
    }
}
