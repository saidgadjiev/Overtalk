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

    private static final UserProfile2 TEST_USER_PROFILE_1 = new UserProfile2();

    private static final UserProfile2 TEST_USER_PROFILE_2 = new UserProfile2();

    private static final Category CATEGORY = new Category();

    private static final Post POST = new Post();

    static {
        TEST_USER_PROFILE_1.setNickName("test");
        TEST_USER_PROFILE_1.setUserName("test");
        TEST_USER_PROFILE_1.setPassword(new BCryptPasswordEncoder().encode("1"));

        TEST_USER_PROFILE_2.setNickName("test1");
        TEST_USER_PROFILE_2.setUserName("test1");
        TEST_USER_PROFILE_2.setPassword(new BCryptPasswordEncoder().encode("1"));

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

            session.create(TEST_USER_PROFILE_1);
            session.create(TEST_USER_PROFILE_2);
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

        mockMvc
                .perform(post("/api/like/post/1")
                        .with(user("test1").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId", is(1)))
                .andExpect(jsonPath("$.likesCount", is(2)))
                .andExpect(jsonPath("$.liked", is(true)));

        try (Session session = sessionManager.createSession()) {
            List<Like> all = session.queryForAll(Like.class);

            Assert.assertEquals(all.size(), 2);
            Like like1 = all.get(0);

            Assert.assertEquals((int) like1.getPost().getId(), 1);
            Assert.assertEquals(like1.getUser().getUserName(), "test");

            Like like2 = all.get(1);

            Assert.assertEquals((int) like2.getPost().getId(), 1);
            Assert.assertEquals(like2.getUser().getUserName(), "test1");
        }
    }

    @Test
    public void dislike() throws Exception {
        createLike(TEST_USER_PROFILE_1);

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

    private void createLike(UserProfile2 userProfile) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            Like like = new Like();
            like.setPost(POST);

            like.setUser(userProfile);

            session.create(like);
        }
    }

}