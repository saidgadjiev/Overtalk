package ru.saidgadjiev.aboutme.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.aboutme.domain.Project;

import java.sql.SQLException;
import java.util.List;

@Repository
public class ProjectDao {

    private SessionManager sessionManager;

    private static final Logger LOGGER = Logger.getLogger(PostDao.class);

    @Autowired
    public ProjectDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public List<Project> getAll() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            return session.queryForAll(Project.class);
        }
    }

    public void create(Project project) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            session.create(project);
        }
    }
}
