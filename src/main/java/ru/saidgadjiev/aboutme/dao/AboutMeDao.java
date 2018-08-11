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

@Repository
public class AboutMeDao {

    private final SessionManager sessionManager;

    @Autowired
    public AboutMeDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public AboutMe getAboutMe() throws SQLException {
        Session session = sessionManager.currentSession();

        return session.statementBuilder().createSelectStatement(AboutMe.class).uniqueResult();
    }

    public int update(AboutMe aboutMe) throws SQLException {
        Session session = sessionManager.currentSession();
        UpdateStatement updateStatement = session.statementBuilder().createUpdateStatement(AboutMe.class);

        updateStatement.set("post", aboutMe.getPost());
        updateStatement.set("placeOfResidence", aboutMe.getPlaceOfResidence());
        updateStatement.where(new Criteria().add(Restrictions.eq("id", 1)));

        return updateStatement.update();
    }
}
