package ru.saidgadjiev.aboutme.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import ru.saidgadjiev.aboutme.configuration.TestConfiguration;
import ru.saidgadjiev.aboutme.domain.*;
import ru.saidgadjiev.ormnext.core.dao.Session;
import ru.saidgadjiev.ormnext.core.dao.SessionManager;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by said on 12.08.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LikeControllerWebSocketIntegrationTest {

    @LocalServerPort
    private int port;

    private String webSocketUrl;

    private static final String WEBSOCKET_TOPIC = "/topic/likes";

    private BlockingQueue<String> blockingQueue;

    private WebSocketStompClient stompClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionManager sessionManager;

    private static final Userprofile TEST_USER_PROFILE_1 = new Userprofile();

    private static final Category CATEGORY = new Category();

    private static final Post POST = new Post();

    static {
        TEST_USER_PROFILE_1.setNickname("test");
        TEST_USER_PROFILE_1.setUsername("test");
        TEST_USER_PROFILE_1.setPassword(new BCryptPasswordEncoder().encode("1"));

        CATEGORY.setName("Test");
        CATEGORY.setDescription("Test");

        POST.setTitle("Test");
        POST.setContent("Test");
        POST.setCategory(CATEGORY);
    }

    @Before
    public void setup() throws Exception {
        try (Session session = sessionManager.createSession()) {
            session.clearTables(Like.class, Userprofile.class, Post.class, Category.class);
            session.statementBuilder().createQuery("ALTER TABLE `like` ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.statementBuilder().createQuery("ALTER TABLE userprofile ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.statementBuilder().createQuery("ALTER TABLE post ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.statementBuilder().createQuery("ALTER TABLE category ALTER COLUMN id RESTART WITH 1").executeUpdate();

            session.create(TEST_USER_PROFILE_1);
            session.create(CATEGORY);
            session.create(POST);
        }

        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
        webSocketUrl = "ws://localhost:" + port + "/aboutme";
    }

    @Test
    public void likeWebSocket() throws Exception {
        StompSession session = stompClient
                .connect(webSocketUrl, new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);

        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());
        mockMvc
                .perform(post("/api/like/post/1")
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                );

        Assert.assertEquals("{\"postId\":1,\"likesCount\":1}", blockingQueue.poll(1, SECONDS));
    }

    @Test
    public void dislikeWebSocket() throws Exception {
        StompSession session = stompClient
                .connect(webSocketUrl, new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);

        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());

        createLike(TEST_USER_PROFILE_1);

        mockMvc
                .perform(post("/api/dislike/post/1")
                        .with(user("test").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                )
                .andExpect(status().isOk());

        Assert.assertEquals("{\"postId\":1,\"likesCount\":0}", blockingQueue.poll(1, SECONDS));
    }

    private void createLike(Userprofile userprofile) throws SQLException {
        try (Session session = sessionManager.createSession()) {
            Like like = new Like();
            like.setPost(POST);

            like.setUser(userprofile);

            session.create(like);
        }
    }

    class DefaultStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            blockingQueue.offer(new String((byte[]) o));
        }
    }

}