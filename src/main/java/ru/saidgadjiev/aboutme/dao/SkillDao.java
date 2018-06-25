package ru.saidgadjiev.aboutme.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.aboutme.domain.Skill;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;

import java.sql.SQLException;

@Repository
public class SkillDao {

    private final SessionManager sessionManager;

    @Autowired
    public SkillDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void create(Skill skill) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            session.create(skill);
        }
    }

    public int update(Skill skill) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            return session.update(skill);
        }
    }

    public int remove(Skill skill) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            return session.delete(skill);
        }
    }
}
