package ru.saidgadjiev.aboutme.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.aboutme.domain.Skill;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Criteria;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Restrictions;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.UpdateStatement;

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

    public int update(Integer id, String name, int percentage) throws SQLException {
        Session session = sessionManager.currentSession();

        UpdateStatement updateStatement = session.statementBuilder().createUpdateStatement(Skill.class);

        updateStatement.set("name", name);
        updateStatement.set("percentage", percentage);
        updateStatement.where(new Criteria().add(Restrictions.eq("id", id)));

        return updateStatement.update();
    }

    public int removeById(Integer id) throws SQLException {
        Session session = sessionManager.currentSession();

        return session.deleteById(Skill.class, id);
    }
}
