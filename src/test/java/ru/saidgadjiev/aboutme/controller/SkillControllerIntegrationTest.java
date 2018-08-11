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
import ru.saidgadjiev.aboutme.common.JsonUtil;
import ru.saidgadjiev.aboutme.configuration.TestConfiguration;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.aboutme.domain.Role;
import ru.saidgadjiev.aboutme.domain.Skill;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by said on 11.08.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
@AutoConfigureMockMvc
public class SkillControllerIntegrationTest {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void before() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            session.clearTables(Skill.class, AboutMe.class);
            session.statementBuilder().createQuery("ALTER TABLE skill ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
    }

    @Test
    public void createSkill() throws Exception {
        createAboutMe();
        Skill skill = getTestSkill();
        Skill except = getTestSkill();

        except.setId(1);

        mockMvc
            .perform(post("/api/skill/create")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.toJson(skill))
                .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
        )
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.toJson(except)));

        try (Session session = sessionManager.createSession()) {
            List<Skill> skills = session.queryForAll(Skill.class);

            Assert.assertEquals(skills.size(), 1);
            Assert.assertEquals(JsonUtil.toJson(skills.get(0)), JsonUtil.toJson(except));
        }
    }

    @Test
    public void createSkillUnauthorized() throws Exception {
        mockMvc
                .perform(post("/api/skill/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JsonUtil.toJson(getTestSkill()))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void removeSkillUnauthorized() throws Exception {
        mockMvc
                .perform(post("/api/skill/delete/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateSkillUnauthorized() throws Exception {
        mockMvc
                .perform(post("/api/skill/update/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JsonUtil.toJson(getTestSkill()))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void removeSkill() throws Exception {
        Skill created = createSkill(createAboutMe());

        mockMvc
                .perform(post("/api/skill/delete/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JsonUtil.toJson(created))
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk());

        try (Session session = sessionManager.createSession()) {
            List<Skill> skills = session.queryForAll(Skill.class);

            Assert.assertTrue(skills.isEmpty());
        }
    }

    @Test
    public void updateSkill() throws Exception {
        Skill created = createSkill(createAboutMe());
        Skill skill = getTestSkill();

        skill.setId(created.getId());
        skill.setName("Spring");
        skill.setPercentage(100);

        mockMvc
                .perform(post("/api/skill/update/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JsonUtil.toJson(skill))
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.toJson(skill)));

        try (Session session = sessionManager.createSession()) {
            Skill result = session.queryForId(Skill.class, created.getId());

            Assert.assertEquals(JsonUtil.toJson(skill), JsonUtil.toJson(result));
        }
    }

    private Skill getTestSkill() {
        Skill skill = new Skill();

        skill.setName("Java");
        skill.setPercentage(90);

        return skill;
    }

    private AboutMe testAboutMe() {
        AboutMe aboutMe = new AboutMe();

        aboutMe.setId(1);
        aboutMe.setFio("Гаджиев Саид Алиевич");
        Calendar dateOfBirthCalendar = new GregorianCalendar(1995, 7, 1);

        aboutMe.setDateOfBirth(dateOfBirthCalendar.getTime());
        aboutMe.setPlaceOfResidence("Москва");
        aboutMe.setPost("Java разработчик");
        aboutMe.setEducationLevel("Бакалавр");


        return aboutMe;
    }

    private AboutMe createAboutMe() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            AboutMe aboutMe = testAboutMe();

            session.create(aboutMe);

            return aboutMe;
        }
    }

    private Skill createSkill(AboutMe aboutMe) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            Skill skill = getTestSkill();

            skill.setAboutMe(aboutMe);

            session.create(skill);

            return skill;
        }
    }

}