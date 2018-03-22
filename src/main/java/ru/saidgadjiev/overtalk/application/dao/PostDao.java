package ru.saidgadjiev.overtalk.application.dao;

import org.apache.log4j.Logger;
import ru.saidgadjiev.orm.next.core.criteria.impl.SelectStatement;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.orm.next.core.stament_executor.GenericResults;
import ru.saidgadjiev.overtalk.application.domain.Post;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by said on 04.03.2018.
 */
public class PostDao {

    private Session session;

    private static final Logger LOGGER = Logger.getLogger(PostDao.class);

    public PostDao(Session session) {
        this.session = session;
    }

    public void create(Post post) throws SQLException {
        LOGGER.debug("create(): " + post.toString());
        session.create(post);
    }

    public List<Post> getAll(int limit, long offset) throws SQLException {
        LOGGER.debug("getAll()");
        SelectStatement<Post> selectStatement = new SelectStatement<>(Post.class);

        selectStatement.limit(limit).offset((int) offset);
        try (GenericResults<Post> genericResults = session.query(selectStatement)) {
            List<Post> posts = genericResults.getResults();

            LOGGER.debug(posts.toString());

            return posts;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public Post getById(int id) throws SQLException {
        LOGGER.debug("getById(): " + id);
        Post post = session.queryForId(id);

        LOGGER.debug(post.toString());
        return post;
    }

    public long countOff() throws SQLException {
        LOGGER.debug("countOffByPostId()");

        return session.countOff();
    }
}
