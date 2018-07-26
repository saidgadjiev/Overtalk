package ru.saidgadjiev.aboutme.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.aboutme.domain.Role;

import java.sql.SQLException;

/**
 * Created by said on 24.03.2018.
 */
@Repository
public class RoleDao {

    private SessionManager sessionManager;

    @Autowired
    public RoleDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public Role queryForId(Integer id) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.queryForId(Role.class, id);
    }
}
