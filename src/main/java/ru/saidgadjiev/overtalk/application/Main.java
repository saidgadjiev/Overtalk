package ru.saidgadjiev.overtalk.application;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import ru.saidgadjiev.orm.next.core.dao.BaseSessionManagerImpl;
import ru.saidgadjiev.orm.next.core.dao.Session;
import ru.saidgadjiev.orm.next.core.dao.SessionManager;
import ru.saidgadjiev.orm.next.core.db.H2DatabaseType;
import ru.saidgadjiev.orm.next.core.support.PolledConnectionSource;
import ru.saidgadjiev.overtalk.application.domain.*;

/**
 * Created by said on 11.03.2018.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        MysqlDataSource dataSource = new MysqlDataSource();

        dataSource.setUser("root");
        dataSource.setPassword("said1995");
        dataSource.setURL("jdbc:mysql://localhost:3306/overtalk");
        SessionManager sessionManager = new BaseSessionManagerImpl(new PolledConnectionSource(dataSource, new H2DatabaseType()));
        Session session = sessionManager.getSession();

        session.createTable(UserProfile.class, true);
        session.createTable(Role.class, true);
        session.createTable(UserRoles.class, true);

        /*Session<Post, Integer> postDao = sessionManager.forClass(Post.class);
        Session<Comment, Integer> commentDao = sessionManager.forClass(Comment.class);
        for (int i = 0; i < 100; ++i) {
            Post post = new Post();

            post.setTitle(RandomStringUtils.randomAlphabetic(8));
            post.setContent(RandomStringUtils.randomAlphabetic(16));
            postDao.create(post);
        }*/
    }
}
