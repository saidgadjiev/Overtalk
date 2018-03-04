package ru.saidgadjiev.overtalk.application.configuration;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.saidgadjiev.orm.next.core.cache.LRUObjectCache;
import ru.saidgadjiev.orm.next.core.dao.BaseSessionManagerImpl;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.orm.next.core.dao.SessionManager;
import ru.saidgadjiev.orm.next.core.db.H2DatabaseType;
import ru.saidgadjiev.orm.next.core.logger.LoggerFactory;
import ru.saidgadjiev.orm.next.core.support.PolledConnectionSource;
import ru.saidgadjiev.orm.next.core.utils.TableUtils;
import ru.saidgadjiev.overtalk.application.model.dao.Comment;
import ru.saidgadjiev.overtalk.application.model.dao.Post;

import java.sql.SQLException;

/**
 * Created by said on 19.02.2018.
 */
@Configuration
public class OrmNextConfiguration {

    @Bean
    @Scope(scopeName = "singleton")
    public SessionManager sessionManager() throws SQLException {
        System.setProperty(LoggerFactory.LOG_ENABLED_PROPERTY, "true");
        JdbcDataSource dataSource = new JdbcDataSource();

        dataSource.setURL("jdbc:h2:mem:h2testdb;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1");
        SessionManager sessionManager = new BaseSessionManagerImpl(new PolledConnectionSource(dataSource, new H2DatabaseType()));

        sessionManager.setObjectCache(new LRUObjectCache(16), Post.class, Comment.class);
        TableUtils.createTable(sessionManager.getDataSource(), Post.class, true);
        TableUtils.createTable(sessionManager.getDataSource(), Comment.class, true);

        return sessionManager;
    }

    @Bean
    public Session<Post, Integer> postSession() throws SQLException {
        return sessionManager().forClass(Post.class);
    }

    @Bean
    public Session<Comment, Integer> commentSession() throws SQLException {
        return sessionManager().forClass(Comment.class);
    }

}
