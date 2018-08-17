package ru.saidgadjiev.aboutme.controller;

import org.hamcrest.Matchers;
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
import ru.saidgadjiev.aboutme.domain.AboutMe;
import ru.saidgadjiev.aboutme.domain.Role;
import ru.saidgadjiev.aboutme.domain.Skill;
import ru.saidgadjiev.aboutme.domain.UserProfile;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;

import java.sql.SQLException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void before() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            session.clearTable(UserProfile.class);
            session.statementBuilder().createQuery("ALTER TABLE userprofile ALTER COLUMN id RESTART WITH 1").executeUpdate();
        }
    }

    @Test
    public void getAll() throws Exception {
        createUser("test", "test");
        createUser("test1", "test1");

        mockMvc.perform(get("/api/users")
                .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].userName", is("test")))
                .andExpect(jsonPath("$.content[0].nickName", is("test")))
                .andExpect(jsonPath("$.content[1].userName", is("test1")))
                .andExpect(jsonPath("$.content[1].nickName", is("test1")));
    }

    @Test
    public void existUserName() throws Exception {
        createUser("test", "test");

        mockMvc.perform(head("/api/userName/exist/test"))
                .andExpect(status().isFound());

        mockMvc.perform(get("/api/userName/exist/test1"))
                .andExpect(status().isOk());
    }

    @Test
    public void existNickName() throws Exception {
        createUser("test", "test");

        mockMvc.perform(head("/api/nickName/exist/test"))
                .andExpect(status().isFound());

        mockMvc.perform(get("/api/nickName/exist/test1"))
                .andExpect(status().isOk());
    }

    private UserProfile createUser(String userName, String nickName) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            UserProfile userProfile = new UserProfile();

            userProfile.setUserName(userName);
            userProfile.setNickName(nickName);
            userProfile.setPassword(new BCryptPasswordEncoder().encode("1"));

            session.create(userProfile);

            return userProfile;
        }
    }
}