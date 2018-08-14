package ru.saidgadjiev.aboutme.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.saidgadjiev.aboutme.configuration.TestConfiguration;
import ru.saidgadjiev.aboutme.domain.*;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;

import java.sql.SQLException;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by said on 12.08.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
@AutoConfigureMockMvc
public class LikeControllerIntegrationTest {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private MockMvc mockMvc;

    private static final UserProfile2 USER_PROFILE = new UserProfile2();

    private static final Category CATEGORY = new Category();

    private static final Post POST = new Post();

    static {
        USER_PROFILE.setNickName("test");
        USER_PROFILE.setUserName("test");
        USER_PROFILE.setPassword(new BCryptPasswordEncoder().encode("1"));

        CATEGORY.setName("Test");
        CATEGORY.setDescription("Test");

        POST.setTitle("Test");
        POST.setContent("Test");
        POST.setCategory(CATEGORY);
    }

    @Before
    public void before() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            session.clearTables(Like.class, UserProfile.class, Post.class, Category.class);
            session.statementBuilder().createQuery("ALTER TABLE `like` ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.statementBuilder().createQuery("ALTER TABLE userprofile ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.statementBuilder().createQuery("ALTER TABLE post ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.statementBuilder().createQuery("ALTER TABLE category ALTER COLUMN id RESTART WITH 1").executeUpdate();

            session.create(USER_PROFILE);
            session.create(CATEGORY);
            session.create(POST);
        }
    }

    @Test
    public void like() throws Exception {
        mockMvc
                .perform(post("/api/like/post/1")
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId", is(1)))
                .andExpect(jsonPath("$.likesCount", is(1)))
                .andExpect(jsonPath("$.liked", is(true)));

        try (Session session = sessionManager.createSession()) {
            List<Like> all = session.queryForAll(Like.class);

            Assert.assertEquals(all.size(), 1);
            Like like = all.get(0);

            Assert.assertEquals((int) like.getPost().getId(), 1);
            Assert.assertEquals(like.getUser().getUserName(), "test");
        }
    }

    @Test
    public void dislike() throws Exception {
        createLike();

        mockMvc
                .perform(post("/api/dislike/post/1")
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId", is(1)))
                .andExpect(jsonPath("$.likesCount", is(0)))
                .andExpect(jsonPath("$.liked", is(false)));

        try (Session session = sessionManager.createSession()) {
            List<Like> all = session.queryForAll(Like.class);

            Assert.assertTrue(all.isEmpty());
        }
    }

    private void createLike() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            Like like = new Like();
            like.setPost(POST);

            like.setUser(USER_PROFILE);

            session.create(like);
        }
    }

}