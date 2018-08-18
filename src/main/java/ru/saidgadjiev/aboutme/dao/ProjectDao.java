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
        Session session = sessionManager.currentSession();

        return session.queryForAll(Project.class);
    }

    public void create(Project project) throws SQLException {
        Session session = sessionManager.currentSession();

        session.create(project);
    }

    public int update(Integer id, Project project) throws SQLException {
        Session session = sessionManager.currentSession();

        project.setId(id);

        return session.update(project);
    }

    public Project getById(Integer id) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.queryForId(Project.class, id);
    }
}
