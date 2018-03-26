package ru.saidgadjiev.overtalk.application.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.orm.next.core.criteria.impl.SelectStatement;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.orm.next.core.dao.SessionManager;
import ru.saidgadjiev.orm.next.core.stament_executor.GenericResults;
import ru.saidgadjiev.overtalk.application.domain.Post;

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
        Session session = sessionManager.getCurrentSession();

        session.create(post);
    }

    public List<Post> getAll(int limit, long offset) throws SQLException {
        LOGGER.debug("getAll()");
        Session session = sessionManager.getCurrentSession();
        SelectStatement<Post> selectStatement = new SelectStatement<>(Post.class);

        selectStatement.limit(limit).offset((int) offset);
        try (GenericResults<Post> genericResults = session.query(selectStatement)) {
            List<Post> posts = genericResults.getResults();

            LOGGER.debug(posts.toString());

            return posts;
        }
    }

    public Post getById(int id) throws SQLException {
        LOGGER.debug("getById(): " + id);
        Session session = sessionManager.getCurrentSession();
        Post post = session.queryForId(Post.class, id);

        LOGGER.debug(post.toString());

        return post;
    }

    public long countOff() throws SQLException {
        LOGGER.debug("countOffByPostId()");
        Session session = sessionManager.getCurrentSession();

        return session.countOff(Post.class);
    }
}
