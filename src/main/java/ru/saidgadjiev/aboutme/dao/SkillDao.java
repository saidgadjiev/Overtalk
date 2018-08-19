package ru.saidgadjiev.aboutme.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
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
        Session session = sessionManager.currentSession();

        session.create(skill);
    }

    public int update(Skill skill) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.update(skill);
    }


    public Skill getById(Integer id) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.queryForId(Skill.class, id);
    }

    public int removeById(Integer id) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.deleteById(Skill.class, id);
    }
}
