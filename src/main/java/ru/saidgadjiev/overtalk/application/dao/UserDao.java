package ru.saidgadjiev.overtalk.application.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.saidgadjiev.orm.next.core.criteria.impl.Criteria;
import ru.saidgadjiev.orm.next.core.criteria.impl.Projections;
import ru.saidgadjiev.orm.next.core.criteria.impl.Restrictions;
import ru.saidgadjiev.orm.next.core.criteria.impl.SelectStatement;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.orm.next.core.dao.SessionManager;
import ru.saidgadjiev.orm.next.core.dao.Transaction;
import ru.saidgadjiev.orm.next.core.stament_executor.DatabaseResults;
import ru.saidgadjiev.orm.next.core.stament_executor.GenericResults;
import ru.saidgadjiev.orm.next.core.stament_executor.result_mapper.ResultsMapper;
import ru.saidgadjiev.overtalk.application.domain.UserProfile;
import ru.saidgadjiev.overtalk.application.domain.UserRole;

import java.sql.SQLException;

/**
 * Created by said on 18.03.2018.
 */
@Repository
public class UserDao {

    private SessionManager sessionManager;

    @Autowired
    public UserDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public UserProfile getByUserName(String userName) throws SQLException {
        Session session = sessionManager.getCurrentSession();
        SelectStatement<UserProfile> selectStatement = new SelectStatement<>(UserProfile.class);

        selectStatement.where(new Criteria().add(Restrictions.eq("userName", userName)));

        try (GenericResults<UserProfile> genericResults = session.query(selectStatement)) {
            return genericResults.getFirstResult();
        }
    }

    public void create(UserProfile userProfile) throws SQLException {
        Session session = sessionManager.getCurrentSession();
        Transaction transaction = session.transaction();

        try {
            transaction.begin();
            transaction.create(userProfile);

            for (UserRole userRole : userProfile.getUserRoles()) {
                transaction.create(userRole);
            }
            transaction.commit();
        } catch (SQLException ex) {
            transaction.rollback();
            throw ex;
        }
    }

    public boolean isExists(String userName) throws SQLException {
        Session session = sessionManager.getCurrentSession();
        SelectStatement<Long> selectStatement = new SelectStatement<>(UserProfile.class);

        selectStatement
                .where(new Criteria().add(Restrictions.eq("userName", userName)))
                .selectProjections(Projections.projectionList().add(Projections.selectFunction(Projections.countStar(), "cnt")));

        try (GenericResults<Long> genericResults = session.query(selectStatement)) {
            return genericResults.getFirstResult(new ResultsMapper<Long>() {
                @Override
                public Long mapResults(DatabaseResults results) throws Exception {
                    return results.getLong("cnt");
                }
            }) > 0;
        }
    }
}
