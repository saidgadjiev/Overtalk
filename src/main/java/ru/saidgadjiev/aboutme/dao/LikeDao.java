package ru.saidgadjiev.aboutme.dao;

import org.springframework.stereotype.Repository;
import ru.saidgadjiev.aboutme.domain.Comment;
import ru.saidgadjiev.aboutme.domain.Like;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Criteria;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.DeleteStatement;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.SelectStatement;

import java.sql.SQLException;

import static ru.saidgadjiev.ormnext.core.query.criteria.impl.Restrictions.eq;
import static ru.saidgadjiev.ormnext.core.query.criteria.impl.Restrictions.isNull;

@Repository
public class LikeDao {

    private final SessionManager sessionManager;

    public LikeDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void create(Like like) throws SQLException {
        Session session = sessionManager.currentSession();

        session.create(like);
    }

    public int deletePostLike(Like like) throws SQLException {
        Session session = sessionManager.currentSession();
        DeleteStatement deleteStatement = session.statementBuilder().createDeleteStatement(Like.class);

        deleteStatement
                .where(new Criteria()
                        .add(eq("post", like.getPost().getId()))
                        .add(eq("user", like.getUser().getUserName())));

        return deleteStatement.delete();
    }

    public long postLikes(Integer postId) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<Like> selectStatement = session.statementBuilder().createSelectStatement(Like.class);

        selectStatement
                .countOff()
                .where(new Criteria()
                        .add(eq("post", postId)));

        return selectStatement.queryForLong();
    }

}
