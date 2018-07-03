package ru.saidgadjiev.aboutme.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Criteria;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Restrictions;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.UpdateStatement;

import java.sql.SQLException;

@Repository
public class AboutMeDao {

    private final SessionManager sessionManager;

    @Autowired
    public AboutMeDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void createOrUpdate(AboutMe aboutMe) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            session.createOrUpdate(aboutMe);
        }
    }

    public AboutMe getAboutMe() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            return session.queryForAll(AboutMe.class).iterator().next();
        }
    }

    public int update(AboutMe aboutMe) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            UpdateStatement updateStatement = new UpdateStatement(AboutMe.class);

            updateStatement.set("post", aboutMe.getPost());
            updateStatement.set("placeOfResidence", aboutMe.getPlaceOfResidence());
            updateStatement.where(new Criteria().add(Restrictions.eq("id", 1)));

            return session.update(updateStatement);
        }
    }
}
