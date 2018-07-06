package ru.saidgadjiev.aboutme.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.aboutme.domain.Category;
import ru.saidgadjiev.aboutme.domain.Post;
import ru.saidgadjiev.aboutme.model.CategoryDetails;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Criteria;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Restrictions;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.SelectStatement;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.UpdateStatement;

import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoryDao {

    private static final Logger LOGGER = Logger.getLogger(CategoryDao.class);

    private final SessionManager sessionManager;

    @Autowired
    public CategoryDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void create(Category category) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            session.create(category);
        }
    }

    public int update(Category category) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            return session.update(category);
        }
    }

    public long countOff() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            return session.countOff(Category.class);
        }
    }

    public List<Category> getCategories(int limit, long offset) throws SQLException {
        LOGGER.debug("getPostsByCategoryId()");
        try (Session session = sessionManager.createSession()) {
            SelectStatement<Category> selectStatement = new SelectStatement<>(Category.class);

            selectStatement.limit(limit).offset((int) offset);

            List<Category> categories = session.list(selectStatement);

            LOGGER.debug(categories.toString());

            return categories;
        }
    }

    public Category getById(Integer id) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            return session.queryForId(Category.class, id);
        }
    }

    public int deleteById(Integer id) throws SQLException {
        LOGGER.debug("deleteById()");
        try (Session session = sessionManager.createSession()) {

            return session.deleteById(Category.class, id);
        }
    }
}
