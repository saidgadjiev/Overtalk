package ru.saidgadjiev.overtalk.application;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.log4j.Logger;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.saidgadjiev.orm.next.core.dao.*;
import ru.saidgadjiev.orm.next.core.db.H2DatabaseType;
import ru.saidgadjiev.orm.next.core.field.DataPersisterManager;
import ru.saidgadjiev.orm.next.core.field.persisters.IntegerDataPersister;
import ru.saidgadjiev.orm.next.core.support.PolledConnectionSource;
import ru.saidgadjiev.overtalk.application.dao.PGDatabaseType;
import ru.saidgadjiev.overtalk.application.dao.SerialTypeDataPersister;
import ru.saidgadjiev.overtalk.application.dao.UserDao;
import ru.saidgadjiev.overtalk.application.domain.*;
import ru.saidgadjiev.overtalk.application.service.UserService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by said on 11.03.2018.
 */
public class PrepareForPostgres {

    private static final Logger LOGGER = Logger.getLogger(PrepareForPostgres.class);

    public static void main(String[] args) throws Exception {
        DataPersisterManager.register(8, new SerialTypeDataPersister());
        SessionManager sessionManager = new BaseSessionManagerImpl(new PolledConnectionSource(createDataSource(), new PGDatabaseType()));
        Session session = sessionManager.getCurrentSession();
        createTables(session);
        createRoles(session);
        Transaction transaction = session.transaction();

        try {
            transaction.begin();
            createUsers(transaction);
            createUserRoles(transaction);
            transaction.commit();
        } catch (SQLException ex) {
            transaction.rollback();

            throw ex;
        }
    }

    private static DataSource createDataSource() {
        PGPoolingDataSource dataSource = new PGPoolingDataSource();

        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDatabaseName("overtalk");

        return dataSource;
    }

    private static void createTables(BaseDao session) throws SQLException {
        LOGGER.debug("Table UserProfile created = " + session.createTable(UserProfile.class, true));
        LOGGER.debug("Table Role created = " + session.createTable(Role.class, true));
        LOGGER.debug("Table UserRole created = " + session.createTable(UserRole.class, true));
    }

    private static void createRoles(BaseDao session) throws SQLException {
        Role roleUser = new Role("ROLE_USER");

        LOGGER.debug("Role ROLE_USER created = " + (session.create(roleUser) > 0) + " with id = " + roleUser.getId());
        Role roleAdmin = new Role("ROLE_ADMIN");

        LOGGER.debug("Role ROLE_ADMIN created = " + (session.create(roleAdmin) > 0) + " with id = " + roleUser.getId());
    }

    private static void createUsers(BaseDao session) throws SQLException {
        UserProfile user = new UserProfile();

        user.setUserName("test");
        user.setPassword(new BCryptPasswordEncoder().encode("1"));
        LOGGER.debug("User test created = " + (session.create(user) > 0) + " with id = " + user.getId());
        UserProfile admin = new UserProfile();

        admin.setUserName("admin");
        admin.setPassword(new BCryptPasswordEncoder().encode("1"));
        LOGGER.debug("User admin created = " + (session.create(admin) > 0) + " with id = " + admin.getId());
    }

    private static void createUserRoles(BaseDao session) throws SQLException {
        Role roleUser = session.queryForId(Role.class, 1);
        UserProfile user = session.queryForId(UserProfile.class, 1);
        UserRole userRole = new UserRole(user, roleUser);

        LOGGER.debug("UserRole for user test created = " + (session.create(userRole) > 0));
        Role roleAdmin = session.queryForId(Role.class, 2);
        UserProfile admin = session.queryForId(UserProfile.class, 2);
        UserRole adminUserRole = new UserRole(admin, roleAdmin);

        LOGGER.debug("UserRole for user admin created = " + (session.create(adminUserRole) > 0));
    }
}
