package ru.saidgadjiev.aboutme.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.aboutme.domain.Userprofile;
import ru.saidgadjiev.aboutme.domain.UserprofileRole;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Criteria;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Restrictions;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.SelectStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by said on 18.03.2018.
 */
@Repository
public class UserProfileDao {

    private SessionManager sessionManager;

    @Autowired
    public UserProfileDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public Userprofile getByUserName(String username) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<Userprofile> selectStatement = session.statementBuilder().createSelectStatement(Userprofile.class);

        selectStatement.where(new Criteria().add(Restrictions.eq("username", username)));

        List<Userprofile> userprofiles = selectStatement.list();

        if (userprofiles.isEmpty()) {
            return null;
        }

        return userprofiles.iterator().next();
    }

    public void create(Userprofile userprofile) throws SQLException {
        Session session = sessionManager.currentSession();
        session.beginTransaction();

        try {
            session.create(userprofile);

            for (UserprofileRole userprofileRole : userprofile.getUserprofileRoles()) {
                session.create(userprofileRole);
            }
            session.commit();
        } catch (SQLException ex) {
            session.rollback();
            throw ex;
        }
    }

    public boolean isExistUsername(String username) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<Userprofile> selectStatement = session.statementBuilder().createSelectStatement(Userprofile.class);

        selectStatement
                .where(new Criteria().add(Restrictions.eq("username", username)))
                .countOff();

        return selectStatement.queryForLong() > 0;
    }

    public boolean isExistNickName(String nickname) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<Userprofile> selectStatement = session.statementBuilder().createSelectStatement(Userprofile.class);

        selectStatement
                .where(new Criteria().add(Restrictions.eq("nickname", nickname)))
                .countOff();

        return selectStatement.queryForLong() > 0;
    }

    public List<Userprofile> getList(int limit, long offset) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<Userprofile> selectStatement = session.statementBuilder().createSelectStatement(Userprofile.class);

        selectStatement.limit(limit).offset((int) offset);

        return selectStatement.list();
    }

    public long countOff() throws SQLException {
        Session session = sessionManager.currentSession();
        return session.countOff(Userprofile.class);
    }
}
