package ru.saidgadjiev.aboutme.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.aboutme.domain.Comment;
import ru.saidgadjiev.aboutme.domain.UserProfile;
import ru.saidgadjiev.aboutme.domain.UserRole;
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

    public UserProfile getByUserName(String userName) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<UserProfile> selectStatement = session.statementBuilder().createSelectStatement(UserProfile.class);

        selectStatement.where(new Criteria().add(Restrictions.eq("userName", userName)));

        List<UserProfile> userProfiles = selectStatement.list();

        if (userProfiles.isEmpty()) {
            return null;
        }

        return userProfiles.iterator().next();
    }

    public void create(UserProfile userProfile) throws SQLException {
        Session session = sessionManager.currentSession();
        session.beginTransaction();

        try {
            session.create(userProfile);

            for (UserRole userRole : userProfile.getUserRoles()) {
                session.create(userRole);
            }
            session.commit();
        } catch (SQLException ex) {
            session.rollback();
            throw ex;
        }
    }

    public boolean isExistUserName(String userName) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<UserProfile> selectStatement = session.statementBuilder().createSelectStatement(UserProfile.class);

        selectStatement
                .where(new Criteria().add(Restrictions.eq("userName", userName)))
                .countOff();

        return selectStatement.queryForLong() > 0;
    }

    public boolean isExistNickName(String nickName) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<UserProfile> selectStatement = session.statementBuilder().createSelectStatement(UserProfile.class);

        selectStatement
                .where(new Criteria().add(Restrictions.eq("nickName", nickName)))
                .countOff();

        return selectStatement.queryForLong() > 0;
    }

    public List<UserProfile> getList(int limit, long offset) throws SQLException {
        Session session = sessionManager.currentSession();
        SelectStatement<UserProfile> selectStatement = session.statementBuilder().createSelectStatement(UserProfile.class);

        selectStatement.limit(limit).offset((int) offset);

        return selectStatement.list();
    }

    public long countOff() throws SQLException {
        Session session = sessionManager.currentSession();
        return session.countOff(UserProfile.class);
    }
}
