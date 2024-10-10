package vnavesnoj.status_ping_controller.it.handler;

import jakarta.websocket.ClientEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import vnavesnoj.status_ping_controller.IT;

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
    private WebSocketClient wsClient;

    @LocalServerPort
    private Integer port;
    private URI uri;

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
}
