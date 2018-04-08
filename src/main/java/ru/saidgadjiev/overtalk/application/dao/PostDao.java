package ru.saidgadjiev.overtalk.application.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.orm.next.core.criteria.impl.SelectStatement;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.orm.next.core.dao.SessionManager;
import ru.saidgadjiev.orm.next.core.stament_executor.DatabaseResults;
import ru.saidgadjiev.orm.next.core.stament_executor.GenericResults;
import ru.saidgadjiev.orm.next.core.stament_executor.result_mapper.ResultsMapper;
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

        try (GenericResults<Post> genericResults = session.query(
                "select post.*, count(comment.id) as comment_count from post left join comment on post.id = comment.post_id group by post.id limit " + limit + " offset " + offset
        )) {
            List<Post> posts = genericResults.getResults(new ResultsMapper<Post>() {
                @Override
                public Post mapResults(DatabaseResults databaseResults) throws Exception {
                    Post post = new Post();

                    post.setId(databaseResults.getInt("id"));
                    post.setTitle(databaseResults.getString("title"));
                    post.setContent(databaseResults.getString("content"));
                    post.setCreatedDate(databaseResults.getDate("createddate"));
                    post.setId(databaseResults.getInt("comment_count"));
                    return null;
                }
            });

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
