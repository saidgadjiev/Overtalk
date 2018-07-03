package ru.saidgadjiev.aboutme.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.aboutme.domain.Post;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Criteria;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Restrictions;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.SelectStatement;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.UpdateStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by said on 04.03.2018.
 */
@Repository
public class PostDao {

    private SessionManager sessionManager;

    private static final Logger LOGGER = Logger.getLogger(PostDao.class);

    @Autowired
    public PostDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void create(Post post) throws SQLException {
        LOGGER.debug("create(): " + post.toString());
        try (Session session = sessionManager.createSession()) {
            session.create(post);
        }
    }

    public List<Post> getPosts(int categoryId, int limit, long offset) throws SQLException {
        LOGGER.debug("getPostsByCategoryId()");
        try (Session session = sessionManager.createSession()) {
            SelectStatement<Post> selectStatement = new SelectStatement<>(Post.class);

            selectStatement.limit(limit).offset((int) offset);
            selectStatement.where(new Criteria()
                    .add(Restrictions.eq("category", categoryId)));

            List<Post> posts = session.list(selectStatement);

            LOGGER.debug(posts.toString());

            return posts;
        }
    }

    public Post getById(int id) throws SQLException {
        LOGGER.debug("getById(): " + id);
        try (Session session = sessionManager.createSession()) {
            Post post = session.queryForId(Post.class, id);

            LOGGER.debug(post.toString());

            return post;
        }
    }

    public long countOff() throws SQLException {
        LOGGER.debug("countOffByPostId()");
        try (Session session = sessionManager.createSession()) {

            return session.countOff(Post.class);
        }
    }

    public int update(Post post) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            UpdateStatement updateStatement = new UpdateStatement(Post.class);

            updateStatement.set("title", post.getTitle());
            updateStatement.set("content", post.getContent());

            updateStatement.where(new Criteria()
                    .add(Restrictions.eq("id", post.getId())));

            return session.update(updateStatement);
        }
    }
}
