package ru.saidgadjiev.overtalk.application.dao;

import org.apache.log4j.Logger;
import ru.saidgadjiev.orm.next.core.criteria.impl.Criteria;
import ru.saidgadjiev.orm.next.core.criteria.impl.Projections;
import ru.saidgadjiev.orm.next.core.criteria.impl.Restrictions;
import ru.saidgadjiev.orm.next.core.criteria.impl.SelectStatement;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.orm.next.core.stament_executor.DatabaseResults;
import ru.saidgadjiev.orm.next.core.stament_executor.GenericResults;
import ru.saidgadjiev.orm.next.core.stament_executor.result_mapper.ResultsMapper;
import ru.saidgadjiev.overtalk.application.domain.Comment;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by said on 04.03.2018.
 */
public class CommentDao {

    private Session<Comment, Integer> session;

    private static final Logger LOGGER = Logger.getLogger(CommentDao.class);

    public CommentDao(Session<Comment, Integer> session) {
        this.session = session;
    }

    public void create(Comment comment) throws SQLException {
        LOGGER.debug("create(): " + comment.toString());
        session.create(comment);
    }

    public List<Comment> getAll() throws SQLException {
        return session.queryForAll();
    }

    public Comment getById(Integer id) throws SQLException {
        return session.queryForId(id);
    }

    public List<Comment> getByPostId(Integer id, int limit, int offset) throws SQLException {
        LOGGER.debug("getByPostId(): " + id);
        SelectStatement<Comment> selectStatement = new SelectStatement<>(Comment.class);

        selectStatement.where(new Criteria().add(Restrictions.eq("post", id))).limit(limit).offset(offset);

        try (GenericResults<Comment> genericResults = session.query(selectStatement)) {
            List<Comment> comments = genericResults.getResults();
            LOGGER.debug(comments.toString());

            return comments;
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public long countOffByPostId(Integer id) throws SQLException {
        LOGGER.debug("countOffByPostId(): " + id);
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
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }
}
