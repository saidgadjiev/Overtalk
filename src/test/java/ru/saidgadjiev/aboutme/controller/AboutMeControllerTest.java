package ru.saidgadjiev.aboutme.controller;

import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.saidgadjiev.aboutme.dao.AboutMeDao;
import ru.saidgadjiev.aboutme.dao.SkillDao;
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.aboutme.domain.Skill;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by said on 09.08.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
@AutoConfigureMockMvc
public class AboutMeControllerTest {

    @Autowired
    private JacksonTester<AboutMe> json;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AboutMeDao aboutMeDao;

    @Autowired
    private SkillDao skillDao;

    @org.junit.Test
    public void getAboutMe() throws Exception {
        AboutMe aboutMe = createAboutMe();

        mockMvc
                .perform(get("/api/aboutMe").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.toJson(aboutMe)));
    }

    @org.junit.Test
    public void update() throws Exception {

    }

    private AboutMe createAboutMe() throws SQLException {
        AboutMe aboutMe = new AboutMe();

        aboutMe.setId(1);
        aboutMe.setFio("Гаджиев Саид Алиевич");
        Calendar dateOfBirthCalendar = new GregorianCalendar(1995, 7, 1);

        aboutMe.setDateOfBirth(dateOfBirthCalendar.getTime());
        aboutMe.setPlaceOfResidence("Москва");
        aboutMe.setPost("Java разработчик");
        aboutMe.setEducationLevel("Бакалавр");
        aboutMeDao.create(aboutMe);

        Skill skill = new Skill();

        skill.setAboutMe(aboutMe);
        skill.setId(1);
        skill.setName("Java");
        skill.setPercentage(90);
        skillDao.create(skill);

        aboutMe.getSkills().add(skill);

        return aboutMeDao.getAboutMe();
    }
}