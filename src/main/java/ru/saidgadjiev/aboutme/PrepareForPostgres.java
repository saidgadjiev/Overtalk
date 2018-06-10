package ru.saidgadjiev.aboutme;

import org.apache.log4j.Logger;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.saidgadjiev.aboutme.configuration.OrmNextConfiguration;
import ru.saidgadjiev.aboutme.dao.TextTypeDataPersister;
import ru.saidgadjiev.aboutme.domain.*;
import ru.saidgadjiev.ormnext.core.connection.source.ConnectionSource;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.ormnext.core.dao.SessionManagerBuilder;
import ru.saidgadjiev.ormnext.core.dao.TableOperation;
import ru.saidgadjiev.ormnext.core.field.DataPersisterManager;
import ru.saidgadjiev.ormnext.core.logger.LoggerFactory;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Criteria;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Restrictions;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.SelectStatement;
import ru.saidgadjiev.ormnext.support.connection.source.PolledConnectionSource;
import ru.saidgadjiev.ormnext.support.datapersister.SerialTypeDataPersister;
import ru.saidgadjiev.ormnext.support.dialect.PgDialect;

import java.sql.SQLException;

import static ru.saidgadjiev.ormnext.core.query.criteria.impl.Restrictions.*;

/**
 * Created by said on 11.03.2018.
 */
public class PrepareForPostgres {

    private static final Logger LOGGER = Logger.getLogger(PrepareForPostgres.class);

    public static void main(String[] args) throws Exception {
        System.setProperty(LoggerFactory.LOG_ENABLED_PROPERTY, "true");
        DataPersisterManager.register(TextTypeDataPersister.TYPE, new TextTypeDataPersister());
        DataPersisterManager.register(SerialTypeDataPersister.SERIAL, new SerialTypeDataPersister());

        try (SessionManager sessionManager = createSessionManager()) {
            try (Session session = sessionManager.createSession()) {
                session.beginTransaction();

                try {
                    createRoles(session);
                    createUsers(session);
                    createUserRoles(session);
                    createTestPostsAndComments(session);
                    session.commit();
                } catch (SQLException ex) {
                    session.rollback();

                    throw ex;
                }
            }
        }
    }

    private static void createRoles(Session session) throws SQLException {
        SelectStatement<Role> selectStatement = new SelectStatement<>(Role.class);

        selectStatement.countOff();
        selectStatement.where(new Criteria().add(eq("name", "ROLE_USER")));
        boolean existUserRole = session.queryForLong(selectStatement) > 0;

        if (!existUserRole) {
            Role roleUser = new Role("ROLE_USER");

            LOGGER.debug("Role ROLE_USER created = " + (session.create(roleUser) > 0) + " with id = " + roleUser.getId());
        }
        selectStatement.setObject(1, "ROLE_ADMIN");
        boolean existAdminRole = session.queryForLong(selectStatement) > 0;

        if (!existAdminRole) {
            Role roleAdmin = new Role("ROLE_ADMIN");

            LOGGER.debug("Role ROLE_ADMIN created = " + (session.create(roleAdmin) > 0) + " with id = " + roleAdmin.getId());
        }
    }

    private static void createUsers(Session session) throws SQLException {
        UserProfile user = new UserProfile();

        user.setNickName("test");
        user.setUserName("test");
        user.setPassword(new BCryptPasswordEncoder().encode("1"));
        LOGGER.debug("User test created = " + (session.create(user) > 0) + " with id = " + user.getId());
        UserProfile admin = new UserProfile();

        admin.setNickName("admin");
        admin.setUserName("admin");
        admin.setPassword(new BCryptPasswordEncoder().encode("1"));
        LOGGER.debug("User admin created = " + (session.create(admin) > 0) + " with id = " + admin.getId());
    }

    private static void createUserRoles(Session session) throws SQLException {
        SelectStatement<Role> roleStatement = new SelectStatement<>(Role.class)
                .where(new Criteria().add(eq("name", "ROLE_USER")));

        Role roleUser = session.uniqueResult(roleStatement);
        SelectStatement<UserProfile> userProfileStatement = new SelectStatement<>(UserProfile.class)
                .where(new Criteria().add(eq("userName", "test")));

        UserProfile user = session.uniqueResult(userProfileStatement);
        UserRole userRole = new UserRole(user, roleUser);

        LOGGER.debug("UserRole for user test created = " + (session.create(userRole) > 0));
        roleStatement.setObject(1, "ROLE_ADMIN");

        Role roleAdmin = session.uniqueResult(roleStatement);

        userProfileStatement.setObject(1, "admin");
        UserProfile admin = session.uniqueResult(userProfileStatement);
        UserRole adminUserRole = new UserRole(admin, roleAdmin);

        LOGGER.debug("UserRole for user admin created = " + (session.create(adminUserRole) > 0));
    }

    private static void createTestPostsAndComments(Session session) throws SQLException {
        Post post = new Post();

        post.setContent("Test content");
        post.setTitle("test title");
        UserProfile userProfile = session.uniqueResult(new SelectStatement<>(UserProfile.class));

        post.setUser(userProfile);
        session.create(post);
        LOGGER.debug("Post created with id: " + session.create(post));
        for (int i = 0; i < 9; ++i) {
            Comment comment = new Comment();

            comment.setUser(userProfile);
            comment.setContent("Test content");
            comment.setPost(post);
            LOGGER.debug("Comment created with id: " + session.create(comment));
        }
    }

    private static SessionManager createSessionManager() throws SQLException {
        return new SessionManagerBuilder()
                .connectionSource(postgreConnectionSource())
                .tableOperation(TableOperation.DROP_CREATE)
                .dialect(new PgDialect())
                .entities(UserProfile.class, Post.class, Comment.class, Role.class,UserRole.class)
                .build();
    }

    private static ConnectionSource postgreConnectionSource() {
        PGPoolingDataSource dataSource = new PGPoolingDataSource();

        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDatabaseName("overtalk");

        return new PolledConnectionSource(dataSource);
    }
}
