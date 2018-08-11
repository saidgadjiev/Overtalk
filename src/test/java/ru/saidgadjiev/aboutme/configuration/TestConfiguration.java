package ru.saidgadjiev.aboutme.configuration;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import ru.saidgadjiev.ormnext.core.dialect.Dialect;
import ru.saidgadjiev.ormnext.core.dialect.H2Dialect;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicInteger;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

    private static final AtomicInteger INDEX = new AtomicInteger();

    @Bean
    @Profile("test")
    public DataSource dataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();

        dataSource.setURL("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");

        return dataSource;
    }

    @Bean
    @Profile("test")
    public Dialect dialect() {
        return new H2Dialect();
    }
}
