package ru.saidgadjiev.overtalk.application.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.orm.next.core.dao.SessionManager;
import ru.saidgadjiev.overtalk.application.domain.Project;

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
        Session session = sessionManager.getCurrentSession();

        return session.queryForAll(Project.class);
    }

}
