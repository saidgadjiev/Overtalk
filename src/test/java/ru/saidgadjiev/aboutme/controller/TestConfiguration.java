package ru.saidgadjiev.aboutme.controller;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import ru.saidgadjiev.ormnext.core.dialect.Dialect;
import ru.saidgadjiev.ormnext.core.dialect.H2Dialect;

import javax.sql.DataSource;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

    @Bean
    public DataSource dataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();

        dataSource.setURL("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");

        return dataSource;
    }

    @Bean
    public Dialect dialect() {
        return new H2Dialect();
    }
}
