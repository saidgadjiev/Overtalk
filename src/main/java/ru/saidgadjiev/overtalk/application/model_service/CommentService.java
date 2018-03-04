package ru.saidgadjiev.overtalk.application.model_service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.overtalk.application.model.dao.Comment;
import ru.saidgadjiev.overtalk.application.model.web.CommentDetails;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by said on 19.02.2018.
 */
@Service
public class CommentService {

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
