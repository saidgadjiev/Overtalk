package ru.saidgadjiev.aboutme.configuration;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.saidgadjiev.aboutme.dao.TextTypeDataPersister;
import ru.saidgadjiev.aboutme.domain.*;
import ru.saidgadjiev.ormnext.core.connection.source.ConnectionSource;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;
import ru.saidgadjiev.ormnext.core.dao.SessionManagerBuilder;
import ru.saidgadjiev.ormnext.core.dao.TableOperation;
import ru.saidgadjiev.ormnext.core.field.DataPersisterManager;
import ru.saidgadjiev.ormnext.core.logger.LoggerFactory;
import ru.saidgadjiev.ormnext.support.connection.source.PolledConnectionSource;
import ru.saidgadjiev.ormnext.support.datapersister.SerialTypeDataPersister;
import ru.saidgadjiev.ormnext.support.dialect.PgDialect;

import java.sql.SQLException;

/**
 * Created by said on 19.02.2018.
 */
@Configuration
public class OrmNextConfiguration {

    static {
        System.setProperty(LoggerFactory.LOG_ENABLED_PROPERTY, "true");
        DataPersisterManager.register(TextTypeDataPersister.TYPE, new TextTypeDataPersister());
        DataPersisterManager.register(SerialTypeDataPersister.SERIAL, new SerialTypeDataPersister());
    }

    @Bean
    @Scope(scopeName = "singleton")
    public SessionManager sessionManager() throws SQLException {
        SessionManager sessionManager = new SessionManagerBuilder()
                .connectionSource(postgreConnectionSource())
                .tableOperation(TableOperation.CREATE)
                .dialect(new PgDialect())
                .entities(
                        UserProfile.class,
                        Post.class,
                        Comment.class,
                        Role.class,
                        UserRole.class,
                        Project.class
                )
                .build();

        sessionManager.enableDefaultCache();

        return sessionManager;
    }

    @Bean
    public ConnectionSource postgreConnectionSource() {
        PGPoolingDataSource dataSource = new PGPoolingDataSource();

        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDatabaseName("overtalk");

        return new PolledConnectionSource(dataSource);
    }

    @Bean
    public ConnectionSource mysqlConnectionSource() {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setUser("root");
        dataSource.setPassword("said1995");
        dataSource.setURL("jdbc:mysql://localhost:3306/overtalk");

        return new PolledConnectionSource(dataSource);
    }

}
