package ru.saidgadjiev.aboutme;

import javafx.geometry.Pos;
import ru.saidgadjiev.aboutme.configuration.OrmNextConfiguration;
import ru.saidgadjiev.aboutme.domain.Post;
import ru.saidgadjiev.aboutme.properties.DataSourceProperties;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        DataSourceProperties properties = new DataSourceProperties();

        properties.setHost("localhost");
        properties.setPort(5432);
        properties.setDatabaseName("overtalk");
        properties.setPassword("postgres");
        properties.setUsername("postgres");
        try (SessionManager manager = new OrmNextConfiguration(properties).sessionManager()) {
            try (Session session = manager.createSession()) {
                Post post = session.queryForId(Post.class, 2);

                post.setTitle("Test");
                session.update(post);
            }
        }
    }
}
