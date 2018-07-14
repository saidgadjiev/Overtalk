package ru.saidgadjiev.aboutme.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.aboutme.domain.Project;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;

import java.sql.SQLException;
import java.util.List;

@Repository
public class ProjectDao {

    private SessionManager sessionManager;

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

    public int update(Project project) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            return session.update(project);
        }
    }
}
