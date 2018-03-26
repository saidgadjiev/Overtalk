package ru.saidgadjiev.overtalk.application.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.orm.next.core.criteria.impl.Criteria;
import ru.saidgadjiev.orm.next.core.criteria.impl.Projections;
import ru.saidgadjiev.orm.next.core.criteria.impl.Restrictions;
import ru.saidgadjiev.orm.next.core.criteria.impl.SelectStatement;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.orm.next.core.dao.SessionManager;
import ru.saidgadjiev.orm.next.core.stament_executor.DatabaseResults;
import ru.saidgadjiev.orm.next.core.stament_executor.GenericResults;
import ru.saidgadjiev.orm.next.core.stament_executor.result_mapper.ResultsMapper;
import ru.saidgadjiev.overtalk.application.domain.Comment;
import ru.saidgadjiev.overtalk.application.domain.Post;

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
        Session session = sessionManager.getCurrentSession();

        session.create(comment);
    }

    public List<Comment> getAll() throws SQLException {
        Session session = sessionManager.getCurrentSession();

        return session.queryForAll(Comment.class);
    }

    public Comment getById(Integer id) throws SQLException {
        Session session = sessionManager.getCurrentSession();

        return session.queryForId(Comment.class, id);
    }

    public List<Comment> getByPostId(Integer id, int limit, int offset) throws SQLException {
        LOGGER.debug("getByPostId(): " + id);
        Session session = sessionManager.getCurrentSession();
        SelectStatement<Comment> selectStatement = new SelectStatement<>(Comment.class);

        selectStatement.where(new Criteria().add(Restrictions.eq("post", id))).limit(limit).offset(offset);

        try (GenericResults<Comment> genericResults = session.query(selectStatement)) {
            List<Comment> comments = genericResults.getResults();
            LOGGER.debug(comments.toString());

            return comments;
        }
    }

    public long countOffByPostId(Integer id) throws SQLException {
        LOGGER.debug("countOffByPostId(): " + id);
        Session session = sessionManager.getCurrentSession();
        SelectStatement<Long> selectStatement = new SelectStatement<>(Comment.class);

        selectStatement
                .selectProjections(
                        Projections.projectionList()
                                .add(Projections.selectFunction(Projections.countStar(), "cnt"))
                )
                .where(new Criteria().add(Restrictions.eq("post", id)));

        try (GenericResults<Long> genericResults = session.query(selectStatement)) {
            Long count = genericResults.getFirstResult(new ResultsMapper<Long>() {
                @Override
                public Long mapResults(DatabaseResults results) throws Exception {
                    return results.getLong("cnt");
                }
            });
            LOGGER.debug(count);

            return count;
        }
    }
}
