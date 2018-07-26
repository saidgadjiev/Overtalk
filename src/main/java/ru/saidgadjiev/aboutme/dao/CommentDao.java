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
        Session session = sessionManager.currentSession();

        session.create(comment);
    }

    public List<Comment> getByPostId(Integer id, int limit, int offset) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<Comment> selectStatement = new SelectStatement<>(Comment.class);

        selectStatement.where(new Criteria().add(Restrictions.eq("post", id))).limit(limit).offset(offset);

        return session.list(selectStatement);
    }

    public long countOffByPostId(Integer id) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<Comment> selectStatement = new SelectStatement<>(Comment.class);

        selectStatement
                .withoutJoins(true)
                .countOff()
                .where(new Criteria().add(Restrictions.eq("post", id)));

        return session.queryForLong(selectStatement);
    }

    public int update(Comment comment) throws SQLException {
        Session session = sessionManager.currentSession();
        UpdateStatement updateStatement = new UpdateStatement(Comment.class);

        updateStatement.set("content", comment.getContent());
        updateStatement.where(new Criteria().add(Restrictions.eq("id", comment.getId())));
        long count = session.update(updateStatement);

        return (int) count;
    }

    public int deleteById(Integer id) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.deleteById(Comment.class, id);
    }
}
