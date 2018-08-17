package ru.saidgadjiev.aboutme.dao;

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

    @Autowired
    public PostDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void create(Post post) throws SQLException {
        Session session = sessionManager.currentSession();
        session.create(post);
    }

    public List<Post> getPosts(int categoryId, int limit, long offset) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<Post> selectStatement = session.statementBuilder().createSelectStatement(Post.class);

        selectStatement.limit(limit).offset((int) offset);
        selectStatement.where(new Criteria()
                .add(Restrictions.eq("category", categoryId)));

        return selectStatement.list();
    }

    public Post getById(int id) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.queryForId(Post.class, id);
    }

    public long countOffPostsByCategoryId(Integer categoryId) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<Post> selectStatement = session.statementBuilder().createSelectStatement(Post.class);

        selectStatement
                .withoutJoins(true)
                .countOff()
                .where(new Criteria().add(Restrictions.eq("category", categoryId)));

        return selectStatement.queryForLong();
    }

    public int update(Post post) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.update(post);
    }

    public int deleteById(Integer id) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.deleteById(Post.class, id);
    }
}
