package ru.saidgadjiev.aboutme.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Criteria;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Restrictions;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.SelectStatement;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.UpdateStatement;

import java.sql.SQLException;
import java.util.List;

@Repository
public class AboutMeDao {

    private final SessionManager sessionManager;

    @Autowired
    public AboutMeDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void create(AboutMe aboutMe) throws SQLException {
        Session session = sessionManager.currentSession();

        session.create(aboutMe);
    }

    public AboutMe getAboutMe() throws SQLException {
        Session session = sessionManager.currentSession();

        return session.uniqueResult(new SelectStatement<>(AboutMe.class));
    }

    public int update(AboutMe aboutMe) throws SQLException {
        Session session = sessionManager.currentSession();
        UpdateStatement updateStatement = new UpdateStatement(AboutMe.class);

        updateStatement.set("post", aboutMe.getPost());
        updateStatement.set("placeOfResidence", aboutMe.getPlaceOfResidence());
        updateStatement.where(new Criteria().add(Restrictions.eq("id", 1)));

        return session.update(updateStatement);
    }
}
