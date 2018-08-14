package ru.saidgadjiev.aboutme.dao;

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

    @Autowired
    public CommentDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void create(Comment comment) throws SQLException {
        Session session = sessionManager.currentSession();

        session.create(comment);
    }

    public List<Comment> getByPostId(Integer id, int limit, long offset) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<Comment> selectStatement = session.statementBuilder().createSelectStatement(Comment.class);

        selectStatement.where(new Criteria().add(Restrictions.eq("post", id))).limit(limit).offset(offset);

        return selectStatement.list();
    }

    public long countOffByPostId(Integer id) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<Comment> selectStatement = session.statementBuilder().createSelectStatement(Comment.class);

        selectStatement
                .withoutJoins(true)
                .countOff()
                .where(new Criteria().add(Restrictions.eq("post", id)));

        return selectStatement.queryForLong();
    }

    public int update(Integer id, String content) throws SQLException {
        Session session = sessionManager.currentSession();
        UpdateStatement updateStatement = session.statementBuilder().createUpdateStatement(Comment.class);

        updateStatement.set("content", content);
        updateStatement.where(new Criteria().add(Restrictions.eq("id", id)));

        return updateStatement.update();
    }

    public int deleteById(Integer id) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.deleteById(Comment.class, id);
    }

    public Comment getById(Integer id) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.queryForId(Comment.class, id);
    }
}
