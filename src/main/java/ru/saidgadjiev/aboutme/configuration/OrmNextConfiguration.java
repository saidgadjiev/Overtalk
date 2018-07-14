package ru.saidgadjiev.aboutme.configuration;

import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.saidgadjiev.aboutme.domain.*;
import ru.saidgadjiev.aboutme.properties.DataSourceProperties;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.ormnext.core.dao.SessionManagerBuilder;
import ru.saidgadjiev.ormnext.core.dao.TableOperation;
import ru.saidgadjiev.ormnext.core.logger.LoggerFactory;
import ru.saidgadjiev.ormnext.support.connection.source.PolledConnectionSource;
import ru.saidgadjiev.ormnext.support.dialect.PgDialect;
import ru.saidgajiev.ormnext.cache.CacheImpl;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by said on 19.02.2018.
 */
@Configuration
public class OrmNextConfiguration {

    private final DataSourceProperties dataSourceProperties;

    static {
        System.setProperty(LoggerFactory.LOG_ENABLED_PROPERTY, "true");
    }

    @Autowired
    public OrmNextConfiguration(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
    }

    @Bean
    @Scope(scopeName = "singleton")
    public SessionManager sessionManager() throws SQLException {
        SessionManager sessionManager = new SessionManagerBuilder()
                .connectionSource(new PolledConnectionSource(dataSource()))
                .tableOperation(TableOperation.CREATE)
                .dialect(new PgDialect())
                .entities(
                        UserProfile.class,
                        Category.class,
                        Post.class,
                        Comment.class,
                        Role.class,
                        UserRole.class,
                        Project.class,
                        Like.class,
                        AboutMe.class,
                        Skill.class
                )
                .build();

        sessionManager.upgrade(new CacheImpl());
        sessionManager.enableDefaultCache();

        return sessionManager;
    }

    @Bean
    public DataSource dataSource() {
        PGPoolingDataSource dataSource = new PGPoolingDataSource();

        dataSource.setServerName(dataSourceProperties.getHost());
        dataSource.setPortNumber(dataSourceProperties.getPort());
        dataSource.setDatabaseName(dataSourceProperties.getDatabaseName());
        dataSource.setUser(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());

        return dataSource;
    }
}
