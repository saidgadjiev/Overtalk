package ru.saidgadjiev.overtalk.application.configuration;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.saidgadjiev.orm.next.core.cache.LRUObjectCache;
import ru.saidgadjiev.orm.next.core.dao.BaseSessionManagerImpl;
import ru.saidgadjiev.orm.next.core.dao.SessionManager;
import ru.saidgadjiev.orm.next.core.db.H2DatabaseType;
import ru.saidgadjiev.orm.next.core.db.MySQLDatabaseType;
import ru.saidgadjiev.orm.next.core.field.DataPersisterManager;
import ru.saidgadjiev.orm.next.core.logger.LoggerFactory;
import ru.saidgadjiev.orm.next.core.support.ConnectionSource;
import ru.saidgadjiev.orm.next.core.support.PolledConnectionSource;
import ru.saidgadjiev.orm.next.core.utils.TableUtils;
import ru.saidgadjiev.overtalk.application.dao.PGDatabaseType;
import ru.saidgadjiev.overtalk.application.dao.SerialTypeDataPersister;
import ru.saidgadjiev.overtalk.application.domain.Comment;
import ru.saidgadjiev.overtalk.application.domain.Post;

import javax.sql.DataSource;
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
        DataPersisterManager.register(8, new SerialTypeDataPersister());
        SessionManager sessionManager = new BaseSessionManagerImpl(mysqlConnectionSource());

        sessionManager.setObjectCache(new LRUObjectCache(16), Post.class, Comment.class);
        TableUtils.createTable(sessionManager.getDataSource(), Post.class, true);
        TableUtils.createTable(sessionManager.getDataSource(), Comment.class, true);

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

        return new PolledConnectionSource(dataSource, new PGDatabaseType());
    }

    @Bean
    public ConnectionSource mysqlConnectionSource() {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setUser("root");
        dataSource.setPassword("said1995");
        dataSource.setURL("jdbc:mysql://localhost:3306/overtalk");

        return new PolledConnectionSource(dataSource, new MySQLDatabaseType());
    }

}
