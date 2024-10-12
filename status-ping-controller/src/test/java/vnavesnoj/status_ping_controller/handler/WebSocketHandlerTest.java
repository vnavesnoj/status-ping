package vnavesnoj.status_ping_controller.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import vnavesnoj.status_ping_controller.UT;
import vnavesnoj.status_ping_controller.config.WebSocketProperties;
import vnavesnoj.status_ping_controller.dto.Status;
import vnavesnoj.status_ping_controller.dto.UserRequestPayload;
import vnavesnoj.status_ping_controller.dto.UserStatusResponsePayload;
import vnavesnoj.status_ping_controller.holder.WebSocketSessionsHolder;
import vnavesnoj.status_ping_service.dto.UserReadDto;
import vnavesnoj.status_ping_service.service.UserService;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@UT
//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class WebSocketHandlerTest {

    @Autowired
    private WebSocketHandler webSocketHandler;
    @Autowired
    private WebSocketSessionsHolder<String> sessionsHolder;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebSocketProperties webSocketProperties;
    private WebSocketClient wsClient;

    @LocalServerPort
    private Integer port;
    private URI uri;

    private UserRequestPayload requestPayloadOnline1 = new UserRequestPayload("dummy1", Status.ONLINE);
    private UserRequestPayload requestPayloadOnline2 = new UserRequestPayload("dummy2", Status.ONLINE);
    private UserRequestPayload requestPayloadOnline3 = new UserRequestPayload("dummy3", Status.ONLINE);
    private UserRequestPayload requestPayloadOnline4 = new UserRequestPayload("dummy4", Status.ONLINE);
    private UserStatusResponsePayload responsePayloadOnline4 = new UserStatusResponsePayload("dummy4", Status.ONLINE, Instant.now());
    private Map<WebSocketSession, TextMessage> responses;

    @SneakyThrows
    @BeforeEach
    void beforeEach() {
        wsClient = new StandardWebSocketClient();
        uri = URI.create("ws://localhost:%s%s".formatted(port, webSocketProperties.getEndpoints()[0]));
        responses = new ConcurrentHashMap<>();
    }

    @SneakyThrows
    @AfterEach
    void afterEach() {
        sessionsHolder.getSessions().clear();
        Thread.sleep(50);
    }

    @SneakyThrows
    @Test
    void checkConnection() {
        try (final var join = wsClient.execute(new TextWebSocketHandler(), null, uri).join()) {
            assertThat(join.isOpen()).isTrue();
        }
    }

    @SneakyThrows
    @Test
    void addSessionAndNicknameToTheSessionHolder() {
        try (final var join = wsClient.execute(new TextWebSocketHandler(), null, uri).join()) {
            final var sessions = sessionsHolder.getSessions();
            assertThat(sessions).isEmpty();
            final WebSocketMessage<String> message =
                    new TextMessage(objectMapper.writeValueAsString(requestPayloadOnline1), true);
            join.sendMessage(message);
            Thread.sleep(100);
            assertThat(sessions)
                    .isNotEmpty()
                    .hasSize(1)
                    .containsKey(requestPayloadOnline1.getPrincipal());
        }
    }

    @SneakyThrows
    @Test
    void notifyOtherConnectionsWhenUserOnline() {
        final var nickname1 = requestPayloadOnline1.getPrincipal();
        final var nickname2 = requestPayloadOnline2.getPrincipal();
        final var nickname4 = requestPayloadOnline4.getPrincipal();
        Mockito.when(userService.findAllConnectionsByUserNickname(nickname4))
                .thenReturn(List.of(
                        new UserReadDto(nickname1, Instant.now()),
                        new UserReadDto(nickname2, Instant.now())));
        final var handler = new TextWebSocketHandler() {
            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                responses.put(session, message);
            }
        };
        try (final var join1 = wsClient.execute(handler, null, uri).join();
             final var join2 = wsClient.execute(handler, null, uri).join();
             final var join3 = wsClient.execute(handler, null, uri).join();
             final var join4 = wsClient.execute(handler, null, uri).join()) {

            final var sessions = sessionsHolder.getSessions();
            assertThat(sessions).isEmpty();

            final WebSocketMessage<String> message1 =
                    new TextMessage(objectMapper.writeValueAsString(requestPayloadOnline1), true);
            final WebSocketMessage<String> message2 =
                    new TextMessage(objectMapper.writeValueAsString(requestPayloadOnline2), true);
            final WebSocketMessage<String> message3 =
                    new TextMessage(objectMapper.writeValueAsString(requestPayloadOnline3), true);
            final WebSocketMessage<String> message4 =
                    new TextMessage(objectMapper.writeValueAsString(requestPayloadOnline4), true);

            final TextMessage response =
                    new TextMessage(objectMapper.writeValueAsString(responsePayloadOnline4), true);

            join1.sendMessage(message1);
            join2.sendMessage(message2);
            join3.sendMessage(message3);
            join4.sendMessage(message4);

            Thread.sleep(100);

            assertThat(sessionsHolder.getSessions())
                    .hasSize(4);

            assertThat(responses)
                    .isNotEmpty()
                    .hasSize(4)
                    .containsKeys(join1, join2);
        }
    }

    @SneakyThrows
    @Test
    void removeSessionAfterConnectionClosed() {
        final var handler = new TextWebSocketHandler();
        try (final var join1 = wsClient.execute(handler, null, uri).join();
             final var join2 = wsClient.execute(handler, null, uri).join()) {
            Thread.sleep(100);
            assertThat(sessionsHolder.getSessions())
                    .isEmpty();

            final WebSocketMessage<String> message1 =
                    new TextMessage(objectMapper.writeValueAsString(requestPayloadOnline1), true);
            final WebSocketMessage<String> message2 =
                    new TextMessage(objectMapper.writeValueAsString(requestPayloadOnline2), true);

            join1.sendMessage(message1);
            join2.sendMessage(message2);
            Thread.sleep(100);

            assertThat(sessionsHolder.getSessions())
                    .hasSize(2)
                    .containsOnlyKeys(requestPayloadOnline1.getPrincipal(), requestPayloadOnline2.getPrincipal());

            join2.close(CloseStatus.GOING_AWAY);
            Thread.sleep(100);

            assertThat(sessionsHolder.getSessions())
                    .hasSize(1)
                    .containsKey(requestPayloadOnline1.getPrincipal());

            join1.close();
            Thread.sleep(100);

            assertThat(sessionsHolder.getSessions())
                    .isEmpty();
        }
    }

    @SneakyThrows
    @Test
    void whenWsMessageExceptionReturnErrorResponsePayload() {
        final var handler = new TextWebSocketHandler() {
            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                responses.put(session, message);
            }
        };
        try (final var join = wsClient.execute(handler, null, uri).join()) {
            join.sendMessage(new TextMessage("dummy"));
            Thread.sleep(100);

            assertThat(responses)
                    .hasSize(1)
                    .containsKey(join);

            final var textMessage = responses.get(join);
            log.info("Response: %s".formatted(textMessage.getPayload()));
        }
    }
}
