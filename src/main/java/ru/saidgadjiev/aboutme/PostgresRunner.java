package ru.saidgadjiev.aboutme;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.saidgadjiev.aboutme.domain.*;
import ru.saidgadjiev.ormnext.core.dao.Dao;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.Criteria;
import ru.saidgadjiev.ormnext.core.query.criteria.impl.SelectStatement;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static ru.saidgadjiev.ormnext.core.query.criteria.impl.Restrictions.eq;


public class PostgresRunner {

    private static final Logger LOGGER = Logger.getLogger(PostgresRunner.class);

    public void run(SessionManager sessionManager) throws Exception {
        try (Session session = sessionManager.createSession()) {
            session.beginTransaction();

            try {
                createRoles(session);
                createUsers(session);
                createUserRoles(session);
                createAboutMe(session);
                session.commit();
            } catch (SQLException ex) {
                session.rollback();

                throw ex;
            }
        }
    }

    private static void createRoles(Session session) throws SQLException {
        SelectStatement<Role> existStatement = new SelectStatement<>(Role.class);

        existStatement.countOff();
        existStatement.where(new Criteria().add(eq("name", "ROLE_USER")));
        boolean existUserRole = session.queryForLong(existStatement) > 0;

        if (!existUserRole) {
            Role roleUser = new Role("ROLE_USER");

            LOGGER.debug("Role ROLE_USER created = " + (session.create(roleUser) > 0) + " with id = " + roleUser.getId());
        } else {
            LOGGER.debug("Role ROLE_USER already exist");
        }
        existStatement.setObject(1, "ROLE_ADMIN");
        boolean existAdminRole = session.queryForLong(existStatement) > 0;

        if (!existAdminRole) {
            Role roleAdmin = new Role("ROLE_ADMIN");

            LOGGER.debug("Role ROLE_ADMIN created = " + (session.create(roleAdmin) > 0) + " with id = " + roleAdmin.getId());
        } else {
            LOGGER.debug("Role ROLE_ADMIN already exist");
        }
    }

    private static void createUsers(Session session) throws SQLException {
        SelectStatement<UserProfile> existStatement = new SelectStatement<>(UserProfile.class);

        existStatement
                .withoutJoins(true)
                .countOff()
                .where(new Criteria().add(eq("userName", "test")));
        boolean existTestUser = session.queryForLong(existStatement) > 0;

        if (!existTestUser) {
            UserProfile user = new UserProfile();

            user.setNickName("test");
            user.setUserName("test");
            user.setPassword(new BCryptPasswordEncoder().encode("1"));
            LOGGER.debug("User test created = " + (session.create(user) > 0) + " with id = " + user.getId());
        } else {
            LOGGER.debug("User test already exist");
        }
        existStatement.setObject(1, "admin");
        boolean existAdminUser = session.queryForLong(existStatement) > 0;

        if (!existAdminUser) {
            UserProfile admin = new UserProfile();

            admin.setNickName("admin");
            admin.setUserName("admin");
            admin.setPassword(new BCryptPasswordEncoder().encode("1"));
            LOGGER.debug("User admin created = " + (session.create(admin) > 0) + " with id = " + admin.getId());
        } else {
            LOGGER.debug("User admin already exist");
        }
    }

    private static void createUserRoles(Session session) throws SQLException {
        SelectStatement<UserRole> existStatement = new SelectStatement<>(UserRole.class);

        existStatement
                .withoutJoins(true)
                .countOff()
                .where(new Criteria()
                        .add(eq("user", "test"))
                        .add(eq("role", "ROLE_USER")));
        boolean existUserRoleTest = session.queryForLong(existStatement) > 0;
        SelectStatement<Role> roleStatement = new SelectStatement<>(Role.class)
                .where(new Criteria().add(eq("name", "ROLE_USER")));
        SelectStatement<UserProfile> userProfileStatement = new SelectStatement<>(UserProfile.class)
                .where(new Criteria().add(eq("userName", "test")));

        if (!existUserRoleTest) {
            Role roleUser = session.uniqueResult(roleStatement);
            UserProfile user = session.uniqueResult(userProfileStatement);
            UserRole userRole = new UserRole(user, roleUser);

            LOGGER.debug("UserRole for user test created = " + (session.create(userRole) > 0));
        } else {
            LOGGER.debug("UserRole for user test already exist");
        }
        existStatement.setObject(1, "admin");
        existStatement.setObject(2, "ROLE_ADMIN");
        boolean existUserRoleAdmin = session.queryForLong(existStatement) > 0;

        if (!existUserRoleAdmin) {
            roleStatement.setObject(1, "ROLE_ADMIN");

            Role roleAdmin = session.uniqueResult(roleStatement);

            userProfileStatement.setObject(1, "admin");
            UserProfile admin = session.uniqueResult(userProfileStatement);
            UserRole adminUserRole = new UserRole(admin, roleAdmin);

            LOGGER.debug("UserRole for user admin created = " + (session.create(adminUserRole) > 0));
        } else {
            LOGGER.debug("UserRole for user admin already exist");
        }
    }

    private static void createAboutMe(Session session) throws SQLException {
        long count = session.countOff(AboutMe.class);

        if (count == 0) {
            AboutMe aboutMe = new AboutMe();

            aboutMe.setId(1);
            aboutMe.setFio("Гаджиев Саид Алиевич");
            Calendar dateOfBirthCalendar = new GregorianCalendar(1995, 7, 1);

            aboutMe.setDateOfBirth(dateOfBirthCalendar.getTime());
            aboutMe.setPlaceOfResidence("Москва");
            aboutMe.setPost("Java разработчик");
            aboutMe.setEducationLevel("Бакалавр");
            session.create(aboutMe);

            LOGGER.debug("About me created " + aboutMe);
            Skill skill = new Skill();

            skill.setAboutMe(aboutMe);
            skill.setId(1);
            skill.setName("Java");
            skill.setPercentage(90);
            session.create(skill);
            LOGGER.debug("About me skill " + skill);
        } else {
            LOGGER.debug("About me already exist");
        }
    }
}
