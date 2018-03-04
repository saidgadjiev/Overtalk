package ru.saidgadjiev.overtalk.application.model_service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.overtalk.application.dao.PostDao;
import ru.saidgadjiev.overtalk.application.model.dao.Post;
import ru.saidgadjiev.overtalk.application.model.web.PostDetails;
import ru.saidgadjiev.overtalk.application.utils.DTOUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by said on 19.02.2018.
 */
@Service
public class PostService {



    private static final Logger LOGGER = Logger.getLogger(PostService.class);

    private PostDao dao;

    @Autowired
    public PostService(PostDao dao) {
        this.dao = dao;
    }

    public void create(Post post) throws SQLException {
        dao.create(post);
        LOGGER.debug("Create post: " + post.toString());
    }

    public List<PostDetails> getAll() throws SQLException {
        List<Post> posts = dao.getAll();
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
