package ru.saidgadjiev.aboutme.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
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
import ru.saidgadjiev.aboutme.utils.JsonUtils;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by said on 12.08.2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CommentControllerWebSocketIntegrationTest {

    @LocalServerPort
    private int port;

    private String webSocketUrl;

    private static final String WEBSOCKET_TOPIC = "/topic/comments";

    private BlockingQueue<String> blockingQueue;

    private WebSocketStompClient stompClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionManager sessionManager;

    private static final UserProfile USER_PROFILE = new UserProfile();

    private static final Category CATEGORY = new Category();

    private static final Post POST = new Post();

    static {
        USER_PROFILE.setNickName("test");
        USER_PROFILE.setUserName("test");
        USER_PROFILE.setPassword(new BCryptPasswordEncoder().encode("1"));

        CATEGORY.setName("Test");
        CATEGORY.setDescription("Test");

        POST.setTitle("Test");
        POST.setContent("Test");
        POST.setCategory(CATEGORY);
    }

    @Before
    public void setup() throws Exception {
        try (Session session = sessionManager.createSession()) {
            session.clearTables(Comment.class, UserProfile.class, Post.class, Category.class);
            session.statementBuilder().createQuery("ALTER TABLE userprofile ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.statementBuilder().createQuery("ALTER TABLE post ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.statementBuilder().createQuery("ALTER TABLE category ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.statementBuilder().createQuery("ALTER TABLE comment ALTER COLUMN id RESTART WITH 1").executeUpdate();

            session.create(USER_PROFILE);
            session.create(CATEGORY);
            session.create(POST);
        }

        blockingQueue = new LinkedBlockingDeque<>();
        stompClient = new WebSocketStompClient(new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
        webSocketUrl = "ws://localhost:" + port + "/aboutMe";
    }

    @Test
    public void updateCommentWebSocket() throws Exception {
        StompSession session = stompClient
                .connect(webSocketUrl, new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);

        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());
        createComment();

        mockMvc
                .perform(patch("/api/comment/1/update/1")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"content\":\"Test1\"}")
                        .with(user("test").password("1").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                );

        String webSocketResult = blockingQueue.poll(1, SECONDS);

        ObjectNode objectNode = JsonUtils.fromJson(webSocketResult, ObjectNode.class);

        Assert.assertEquals(objectNode.get("status").asInt(), 206);
        Assert.assertEquals(objectNode.get("content").get("commentId").asInt(), 1);
        Assert.assertEquals(objectNode.get("content").get("postId").asInt(), 1);
        Assert.assertEquals(objectNode.get("content").get("content").asText(), "Test1");
    }

    @Test
    public void createCommentWebSocket() throws Exception {
        StompSession session = stompClient
                .connect(webSocketUrl, new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);

        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());
        mockMvc
                .perform(post("/api/comment/1/create")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"content\":\"Test\"}")
                        .with(user("test").password("1").authorities(new SimpleGrantedAuthority(Role.ROLE_ADMIN)))
                );

        String webSocketResult = blockingQueue.poll(1, SECONDS);

        ObjectNode objectNode = JsonUtils.fromJson(webSocketResult, ObjectNode.class);

        Assert.assertEquals(objectNode.get("status").asInt(), 201);
        Assert.assertEquals(objectNode.get("content").get("commentId").asInt(), 1);
        Assert.assertEquals(objectNode.get("content").get("content").asText(), "Test");
        Assert.assertEquals(objectNode.get("content").get("nickName").asText(), "test");
        Assert.assertEquals(objectNode.get("content").get("commentsCount").asInt(), 1);
    }

    private void createComment() throws SQLException {
        try (Session session = sessionManager.createSession()) {
            Comment comment = new Comment();

            comment.setContent("Test");
            comment.setPost(POST);
            comment.setUser(USER_PROFILE);

            session.create(comment);
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