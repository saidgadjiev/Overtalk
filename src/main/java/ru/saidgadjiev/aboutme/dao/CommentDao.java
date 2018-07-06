package ru.saidgadjiev.aboutme.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.aboutme.domain.Comment;
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
public class CommentDao {

    private SessionManager sessionManager;

    private static final Logger LOGGER = Logger.getLogger(CommentDao.class);

    @Autowired
    public CommentDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void create(Comment comment) throws SQLException {
        LOGGER.debug("create(): " + comment.toString());
        try (Session session = sessionManager.createSession()) {
            session.create(comment);
        }
    }

    public List<Comment> getAll() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            return session.queryForAll(Comment.class);
        }
    }

    public Comment getById(Integer id) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            return session.queryForId(Comment.class, id);
        }
    }

    public List<Comment> getByPostId(Integer id, int limit, int offset) throws SQLException {
        LOGGER.debug("getByPostId(): " + id);
        try (Session session = sessionManager.createSession()) {
            SelectStatement<Comment> selectStatement = new SelectStatement<>(Comment.class);

            selectStatement.where(new Criteria().add(Restrictions.eq("post", id))).limit(limit).offset(offset);

            List<Comment> comments = session.list(selectStatement);
            LOGGER.debug(comments.toString());

            return comments;
        }
    }

    public long countOffByPostId(Integer id) throws SQLException {
        LOGGER.debug("countOffByPostId(): " + id);
        try (Session session = sessionManager.createSession()) {
            SelectStatement<Comment> selectStatement = new SelectStatement<>(Comment.class);

            selectStatement
                    .countOff()
                    .where(new Criteria().add(Restrictions.eq("post", id)));

            Long count = session.queryForLong(selectStatement);
            LOGGER.debug(count);

            return count;
        }
    }

    public int update(Comment comment) throws SQLException {
        LOGGER.debug("update(): " + comment.getId());
        try (Session session = sessionManager.createSession()) {
            UpdateStatement updateStatement = new UpdateStatement(Comment.class);

            updateStatement.set("content", comment.getContent());
            updateStatement.where(new Criteria().add(Restrictions.eq("id", comment.getId())));
            long count = session.update(updateStatement);
            LOGGER.debug(count);

            return (int) count;
        }
    }

    public int deleteById(Integer id) throws SQLException {
        LOGGER.debug("deleteById(): " + id);
        try (Session session = sessionManager.createSession()) {
            return session.deleteById(Comment.class, id);
        }
    }
}
