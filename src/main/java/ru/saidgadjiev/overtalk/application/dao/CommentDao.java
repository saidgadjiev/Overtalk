package ru.saidgadjiev.overtalk.application.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.overtalk.application.model.dao.Comment;
import ru.saidgadjiev.overtalk.application.model.web.CommentDetails;
import ru.saidgadjiev.overtalk.application.model_service.CommentService;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by said on 04.03.2018.
 */
public class CommentDao {


    private Session<Comment, Integer> session;

    private static final Logger LOGGER = Logger.getLogger(CommentService.class);

    @Autowired
    public CommentService(Session<Comment, Integer> session) {
        this.session = session;
    }

    public void create(Comment comment) throws SQLException {
        session.create(comment);
        LOGGER.debug("Create comment: " + comment.toString());
    }

    public List<CommentDetails> getAll() throws SQLException {
        List<Comment> comments = session.queryForAll();

        return null;
    }

    public CommentDetails getById(int id) throws SQLException {
        Comment comment = session.queryForId(id);

        return null;
    }
}
