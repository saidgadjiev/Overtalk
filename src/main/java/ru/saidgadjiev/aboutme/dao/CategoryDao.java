package ru.saidgadjiev.aboutme.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.aboutme.domain.Category;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.SelectStatement;

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
        Session session = sessionManager.currentSession();

        session.create(category);
    }

    public int update(Category category) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.update(category);
    }

    public long countOff() throws SQLException {
        Session session = sessionManager.currentSession();

        return session.countOff(Category.class);
    }

    public List<Category> getCategories(int limit, long offset) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<Category> selectStatement = new SelectStatement<>(Category.class);

        selectStatement.limit(limit).offset((int) offset);

        return session.list(selectStatement);
    }

    public Category getById(Integer id) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.queryForId(Category.class, id);
    }

    public int deleteById(Integer id) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.deleteById(Category.class, id);
    }
}
