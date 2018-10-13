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
import ru.saidgadjiev.aboutme.domain.Aboutme;
import ru.saidgadjiev.aboutme.utils.JsonUtils;
import ru.saidgadjiev.aboutme.configuration.TestConfiguration;
import ru.saidgadjiev.aboutme.domain.Role;
import ru.saidgadjiev.aboutme.domain.Skill;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by said on 09.08.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
@AutoConfigureMockMvc
public class AboutmeControllerIntegrationTest {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void before() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            session.clearTables(Skill.class, Aboutme.class);
            session.statementBuilder().createQuery("ALTER TABLE skill ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
    }

    @Test
    public void getAboutMe() throws Exception {
        Aboutme aboutme = createAboutMe();

        createSkill(aboutme);

        mockMvc
                .perform(get("/api/aboutme").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fio", is("Гаджиев Саид Алиевич")))
                .andExpect(jsonPath("$.placeOfResidence", is("Москва")))
                .andExpect(jsonPath("$.educationLevel", is("Бакалавр")))
                .andExpect(jsonPath("$.post", is("Java разработчик")))
                .andExpect(jsonPath("$.skills", hasSize(1)))
                .andExpect(jsonPath("$.skills[0].id", is(1)))
                .andExpect(jsonPath("$.skills[0].name", is("Java")));
    }

    @Test
    public void update() throws Exception {
        createAboutMe();

        mockMvc.perform(patch("/api/aboutme/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"post\":\"Test2\",\"placeOfResidence\":\"Test1\"}")
                .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.post", is("Test2")))
                .andExpect(jsonPath("$.placeOfResidence", is("Test1")));

        try (Session session = sessionManager.createSession()) {
            Aboutme result = session.statementBuilder().createSelectStatement(Aboutme.class).uniqueResult();
            Aboutme expect = getTestAboutMe();

            expect.setPlaceOfResidence("Test1");
            expect.setPost("Test2");

            Assert.assertEquals(JsonUtils.toJson(expect), JsonUtils.toJson(result));
        }
    }

    @Test
    public void updateWithUnauthorized() throws Exception {
        mockMvc.perform(patch("/api/aboutme/update")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtils.toJson(getTestAboutMe()))
        )
                .andExpect(status().isUnauthorized());
    }

    private void createSkill(Aboutme aboutme) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            Skill skill = getTestSkill();

            skill.setAboutme(aboutme);

            session.create(skill);
            aboutme.getSkills().add(skill);
        }
    }

    private Aboutme createAboutMe() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            Aboutme aboutme = getTestAboutMe();

            session.create(aboutme);

            return aboutme;
        }
    }

    private Aboutme getTestAboutMe() {
        Aboutme aboutme = new Aboutme();

        aboutme.setId(1);
        aboutme.setFio("Гаджиев Саид Алиевич");
        aboutme.setDateOfBirth(LocalDate.of(1995, 7, 1));
        aboutme.setPlaceOfResidence("Москва");
        aboutme.setPost("Java разработчик");
        aboutme.setEducationLevel("Бакалавр");

        return aboutme;
    }

    private Skill getTestSkill() {
        Skill skill = new Skill();

        skill.setName("Java");

        return skill;
    }
}