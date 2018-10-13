package ru.saidgadjiev.aboutme.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.aboutme.domain.Aboutme;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;

import java.sql.SQLException;

@Repository
public class AboutMeDao {

    private final SessionManager sessionManager;

    @Autowired
    public AboutMeDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public Aboutme getAboutMe() throws SQLException {
        Session session = sessionManager.currentSession();

        return session.statementBuilder().createSelectStatement(Aboutme.class).uniqueResult();
    }

    public int update(Aboutme aboutme) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.update(aboutme);
    }
}
