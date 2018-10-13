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

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by said on 12.08.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
@AutoConfigureMockMvc
public class CommentControllerIntegrationTest {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private MockMvc mockMvc;

    private static final Userprofile USER_PROFILE = new Userprofile();

    private static final Category CATEGORY = new Category();

    private static final Post POST = new Post();

    static {
        USER_PROFILE.setNickname("test");
        USER_PROFILE.setUsername("test");
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
            session.clearTables(Comment.class, Userprofile.class, Post.class, Category.class);
            session.statementBuilder().createQuery("ALTER TABLE userprofile ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.statementBuilder().createQuery("ALTER TABLE post ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.statementBuilder().createQuery("ALTER TABLE category ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.statementBuilder().createQuery("ALTER TABLE comment ALTER COLUMN id RESTART WITH 1").executeUpdate();

            session.create(USER_PROFILE);
            session.create(CATEGORY);
            session.create(POST);
        }
    }

    @Test
    public void getCommentsByPost() throws Exception {
        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        Comment comment1 = createComment();
        Comment comment2 = createComment();

        mockMvc
                .perform(get("/api/comment/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].content", is("Test")))
                .andExpect(jsonPath("$.content[0].nickname", is("test")))
                .andExpect(jsonPath("$.content[0].createdDate", is(simpleDateFormat.format(comment1.getCreatedDate()))))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].content", is("Test")))
                .andExpect(jsonPath("$.content[1].nickname", is("test")))
                .andExpect(jsonPath("$.content[1].createdDate", is(simpleDateFormat.format(comment2.getCreatedDate()))))
                .andExpect(jsonPath("$.totalElements", is(2)));;
    }

    @Test
    public void createCommentOfPost() throws Exception {
        mockMvc
                .perform(post("/api/comment/1/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"content\":\"Test\"}")
                        .with(user("test").password("1").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nickname", is("test")))
                .andExpect(jsonPath("$.content", is("Test")));

        try (Session session = sessionManager.createSession()) {
            List<Comment> comments = session.queryForAll(Comment.class);

            Assert.assertEquals(comments.size(), 1);

            Comment comment = comments.get(0);

            Assert.assertEquals(comment.getContent(), "Test");
        }
    }

    @Test
    public void create401() throws Exception {
        mockMvc
                .perform(post("/api/comment/1/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"name\":\"Test2\",\"description\":\"Test1\"}")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateComment() throws Exception {
        createComment();

        mockMvc
                .perform(patch("/api/comment/1/update/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"content\":\"Test1\"}")
                        .with(user("test").password("1").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("Test1")));

        try (Session session = sessionManager.createSession()) {
            Comment comment = session.queryForId(Comment.class, 1);

            Assert.assertEquals(comment.getContent(), "Test1");
        }
    }

    @Test
    public void update401() throws Exception {
        mockMvc
                .perform(patch("/api/comment/1/update/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"name\":\"Test2\",\"description\":\"Test1\"}")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteComment() throws Exception {
        createComment();

        mockMvc
                .perform(delete("/api/comment/1/delete/1")
                        .with(user("test").password("1").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk());

        try (Session session = sessionManager.createSession()) {
            Assert.assertTrue(session.queryForAll(Comment.class).isEmpty());
        }
    }


    private Comment createComment() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            Comment comment = new Comment();

            comment.setContent("Test");
            comment.setPost(POST);
            comment.setUser(USER_PROFILE);

            session.create(comment);

            return comment;
        }
    }

}