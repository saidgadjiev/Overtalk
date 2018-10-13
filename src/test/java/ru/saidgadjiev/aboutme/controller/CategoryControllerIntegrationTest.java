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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.saidgadjiev.aboutme.utils.JsonUtils;
import ru.saidgadjiev.aboutme.configuration.TestConfiguration;
import ru.saidgadjiev.aboutme.domain.Category;
import ru.saidgadjiev.aboutme.domain.Role;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by said on 11.08.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
@AutoConfigureMockMvc
public class CategoryControllerIntegrationTest {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void before() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            session.clearTable(Category.class);
            session.statementBuilder().createQuery("ALTER TABLE category ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
    }

    @Test
    public void create() throws Exception {
        mockMvc
                .perform(post("/api/category/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"name\":\"Test2\",\"description\":\"Test1\"}")
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Test2\",\"description\":\"Test1\"}"));

        try (Session session = sessionManager.createSession()) {
            List<Category> categories = session.queryForAll(Category.class);

            Assert.assertEquals(categories.size(), 1);
            Assert.assertEquals(JsonUtils.toJson(categories.get(0)), "{\"id\":1,\"name\":\"Test2\",\"description\":\"Test1\",\"index\":0,\"posts\":[]}");
        }
    }

    @Test
    public void update() throws Exception {
        createCategory();

        mockMvc
                .perform(patch("/api/category/update/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"name\":\"Test2\",\"description\":\"Test1\"}")
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"Test2\",\"description\":\"Test1\"}"));

        try (Session session = sessionManager.createSession()) {
            List<Category> categories = session.queryForAll(Category.class);

            Assert.assertEquals(categories.size(), 1);
            Assert.assertEquals(JsonUtils.toJson(categories.get(0)), "{\"id\":1,\"name\":\"Test2\",\"description\":\"Test1\",\"index\":0,\"posts\":[]}");
        }
    }

    @Test
    public void getCategories() throws Exception {
        createCategory();
        createCategory();

        mockMvc
                .perform(get("/api/category?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Test")))
                .andExpect(jsonPath("$.content[0].description", is("Test")))
                .andExpect(jsonPath("$.content[0].postsCount", is(0)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].name", is("Test")))
                .andExpect(jsonPath("$.content[1].description", is("Test")))
                .andExpect(jsonPath("$.content[1].postsCount", is(0)))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    public void getCategory() throws Exception {
        createCategory();

        mockMvc
                .perform(get("/api/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test")))
                .andExpect(jsonPath("$.description", is("Test")))
                .andExpect(jsonPath("$.postsCount", is(0)));
    }

    @Test
    public void deleteCategory() throws Exception {
        createCategory();

        try (Session session = sessionManager.createSession()) {
            List<Category> categories1 = session.queryForAll(Category.class);

            Assert.assertEquals(categories1.size(), 1);

            mockMvc
                    .perform(delete("/api/category/delete/1")
                            .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                    )
                    .andExpect(status().isOk());

            List<Category> categories2 = session.queryForAll(Category.class);

            Assert.assertTrue(categories2.isEmpty());
        }
    }

    private void createCategory() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            Category category = getTestCategory();

            session.create(category);
        }
    }

    private Category getTestCategory() {
        Category category = new Category();

        category.setName("Test");
        category.setDescription("Test");

        return category;
    }
}