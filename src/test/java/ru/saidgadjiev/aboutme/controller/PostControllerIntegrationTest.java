package ru.saidgadjiev.aboutme.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.saidgadjiev.aboutme.configuration.TestConfiguration;
import ru.saidgadjiev.aboutme.domain.*;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by said on 12.08.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
@AutoConfigureMockMvc
public class PostControllerIntegrationTest {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private MockMvc mockMvc;

    private static final Userprofile USER_PROFILE = new Userprofile();

    private static final Category CATEGORY = new Category();

    static {
        USER_PROFILE.setNickname("Test");
        USER_PROFILE.setUsername("Test");
        USER_PROFILE.setPassword(new BCryptPasswordEncoder().encode("1"));

        CATEGORY.setName("Test");
        CATEGORY.setDescription("Test");
    }

    @Before
    public void before() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            session.clearTables(Post.class, Category.class);
            session.statementBuilder().createQuery("ALTER TABLE post ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.statementBuilder().createQuery("ALTER TABLE category ALTER COLUMN id RESTART WITH 1").executeUpdate();

            session.create(CATEGORY);
        }
    }

    @Test
    public void createPostOfCategory() throws Exception {
        mockMvc
                .perform(post("/api/post/1/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"title\":\"Test2\",\"content\":\"Test1\"}")
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk());

        try (Session session = sessionManager.createSession()) {
            List<Post> posts = session.queryForAll(Post.class);

            Assert.assertEquals(posts.size(), 1);

            Post post = posts.get(0);

            Assert.assertEquals(post.getTitle(), "Test2");
            Assert.assertEquals(post.getContent(), "Test1");
        }
    }

    @Test
    public void update() throws Exception {
        createPost();

        mockMvc
                .perform(patch("/api/post/update/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"title\":\"Test2\",\"content\":\"Test1\"}")
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"title\":\"Test2\",\"content\":\"Test1\"}"));

        try (Session session = sessionManager.createSession()) {
            Post post = session.queryForId(Post.class, 1);

            Assert.assertEquals(post.getTitle(), "Test2");
            Assert.assertEquals(post.getContent(), "Test1");
        }
    }

    @Test
    public void getPostsOfCategory() throws Exception {
        createPost();
        createPost();

        mockMvc
                .perform(get("/api/post/1/posts?page=0&size=10")
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title", is("Test")))
                .andExpect(jsonPath("$.content[0].content", is("Test")))
                .andExpect(jsonPath("$.content[0].commentsCount", is(0)))
                .andExpect(jsonPath("$.content[0].likesCount", is(0)))
                .andExpect(jsonPath("$.content[0].liked", is(false)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].title", is("Test")))
                .andExpect(jsonPath("$.content[1].content", is("Test")))
                .andExpect(jsonPath("$.content[1].commentsCount", is(0)))
                .andExpect(jsonPath("$.content[1].likesCount", is(0)))
                .andExpect(jsonPath("$.content[1].liked", is(false)))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    public void getPost() throws Exception {
        createPost();

        mockMvc
                .perform(get("/api/post/1")
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test")))
                .andExpect(jsonPath("$.content", is("Test")))
                .andExpect(jsonPath("$.commentsCount", is(0)))
                .andExpect(jsonPath("$.likesCount", is(0)))
                .andExpect(jsonPath("$.liked", is(false)));
    }

    @Test
    public void deletePost() throws Exception {
        createPost();

        mockMvc
                .perform(delete("/api/post/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk());

        try (Session session = sessionManager.createSession()) {
            Assert.assertTrue(session.queryForAll(Post.class).isEmpty());
        }
    }

    private void createPost() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            Post post = new Post();

            post.setTitle("Test");
            post.setContent("Test");
            post.setCategory(CATEGORY);

            session.create(post);
        }
    }
}