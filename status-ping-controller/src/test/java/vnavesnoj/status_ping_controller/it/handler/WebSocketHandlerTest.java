package vnavesnoj.status_ping_controller.it.handler;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketHandler;
import vnavesnoj.status_ping_controller.IT;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@IT
public class WebSocketHandlerTest {

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Test
    void init() {
    }
}
