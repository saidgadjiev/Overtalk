package ru.saidgadjiev.aboutme.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.saidgadjiev.aboutme.configuration.TestConfiguration;
import ru.saidgadjiev.aboutme.domain.Project;
import ru.saidgadjiev.aboutme.domain.Role;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
@AutoConfigureMockMvc
public class ProjectControllerIntegrationTest {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void before() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            session.clearTable(Project.class);
            session.statementBuilder().createQuery("ALTER TABLE project ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
    }

    @Test
    public void getAll() throws Exception {
        createProject("Test");
        createProject("Test1");

        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/project"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test")))
                .andExpect(jsonPath("$[0].description", is("Test")))
                .andExpect(jsonPath("$[0].logoPath", is("Test")))
                .andExpect(jsonPath("$[0].projectLink", is("Test")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Test1")))
                .andExpect(jsonPath("$[1].description", is("Test")))
                .andExpect(jsonPath("$[1].logoPath", is("Test")))
                .andExpect(jsonPath("$[1].projectLink", is("Test")));
    }

    @Test
    public void create() throws Exception {
        mockMvc
                .perform(post("/api/project/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"name\":\"Test2\",\"description\":\"Test1\"}")
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN))))
                .andExpect(status().isOk());

        try (Session session = sessionManager.createSession()) {
            Project project = session.queryForAll(Project.class).iterator().next();

            Assert.assertEquals(project.getName(), "Test2");
            Assert.assertEquals(project.getDescription(), "Test1");
        }
    }

    @Test
    public void update() throws Exception {
        createProject("Test");

        mockMvc
                .perform(multipart("/api/project/update/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"name\":\"Test2\",\"description\":\"Test1\"}")
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test2")))
                .andExpect(jsonPath("$.description", is("Test1")));

        try (Session session = sessionManager.createSession()) {
            Project project = session.queryForAll(Project.class).iterator().next();

            Assert.assertEquals(project.getName(), "Test2");
            Assert.assertEquals(project.getDescription(), "Test1");
        }
    }

    private void createProject(String name) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            Project project = new Project();

            project.setName(name);
            project.setDescription("Test");
            project.setProjectLink("Test");
            project.setLogoPath("Test");

            session.create(project);
        }
    }
}