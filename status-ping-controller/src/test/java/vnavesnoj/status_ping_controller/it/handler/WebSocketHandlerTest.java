package vnavesnoj.status_ping_controller.it.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.ClientEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import vnavesnoj.status_ping_controller.IT;
import vnavesnoj.status_ping_controller.dto.Status;
import vnavesnoj.status_ping_controller.dto.UserRequestPayload;
import vnavesnoj.status_ping_controller.holder.WebSocketSessionsHolder;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@IT
@ClientEndpoint
public class WebSocketHandlerTest {

    @Autowired
    private WebSocketHandler webSocketHandler;
    @Autowired
    private WebSocketSessionsHolder<String> sessionsHolder;
    @Autowired
    private ObjectMapper objectMapper;
    private WebSocketClient wsClient;

    @LocalServerPort
    private Integer port;
    private URI uri;

    private UserRequestPayload requestPayloadOnline1 = new UserRequestPayload("dummy1", Status.ONLINE);
    private String jsonRequestPayloadOnline1;

    @SneakyThrows
    @BeforeEach
    void beforeEach() {
        wsClient = new StandardWebSocketClient();
        uri = URI.create("ws://localhost:%s/status".formatted(port));
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
            System.out.println(message.getPayload());
            join.sendMessage(message);
            Thread.sleep(100);
            assertThat(sessions)
                    .isNotEmpty()
                    .hasSize(1)
                    .containsKey(requestPayloadOnline1.getNickname());
        }
    }
}
