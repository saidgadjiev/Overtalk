package ru.saidgadjiev.overtalk.application.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.overtalk.application.model.dao.Post;
import ru.saidgadjiev.overtalk.application.model.web.PostDetails;
import ru.saidgadjiev.overtalk.application.model_service.PostService;
import ru.saidgadjiev.overtalk.application.utils.DTOUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by said on 04.03.2018.
 */
public class PostDao {


    private Session<Post, Integer> session;

    private static final Logger LOGGER = Logger.getLogger(PostService.class);

    @Autowired
    public PostService(Session<Post, Integer> session) {
        this.session = session;
    }

    public void create(Post post) throws SQLException {
        session.create(post);
        LOGGER.debug("Create post: " + post.toString());
    }

    public List<PostDetails> getAll() throws SQLException {
        List<Post> posts = session.queryForAll();
        List<PostDetails> postDetails = new ArrayList<>();

        for (Post post: posts) {
            postDetails.add(DTOUtils.convert(post));
        }

        return postDetails;
    }

    public PostDetails getById(int id) throws SQLException {
        Post post = session.queryForId(id);

        return DTOUtils.convert(post);
    }
}
