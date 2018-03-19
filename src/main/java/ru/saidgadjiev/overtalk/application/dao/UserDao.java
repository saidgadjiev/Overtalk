package ru.saidgadjiev.overtalk.application.dao;

import ru.saidgadjiev.orm.next.core.criteria.impl.Criteria;
import ru.saidgadjiev.orm.next.core.criteria.impl.Restrictions;
import ru.saidgadjiev.orm.next.core.criteria.impl.SelectStatement;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.orm.next.core.stament_executor.GenericResults;
import ru.saidgadjiev.overtalk.application.domain.UserProfile;

import java.sql.SQLException;

/**
 * Created by said on 18.03.2018.
 */
public class UserDao {

    private Session<UserProfile, Integer> session;

    public UserDao(Session<UserProfile, Integer> session) {
        this.session = session;
    }

    public UserProfile getByUserName(String userName) throws SQLException {
        SelectStatement<UserProfile> selectStatement = new SelectStatement<>(UserProfile.class);

        selectStatement.where(new Criteria().add(Restrictions.eq("userName", userName)));

        try (GenericResults<UserProfile> genericResults = session.query(selectStatement)) {
            return genericResults.getFirstResult();
        } catch (Exception ex) {
            throw new SQLException(ex);
        }
    }

    public void create(UserProfile userProfile) {

    }

    public boolean isExists(String userName) {
        return false;
    }
}
