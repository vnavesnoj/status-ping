package vnavesnoj.status_ping_controller.it.handler;

import lombok.Value;
import org.springframework.web.socket.WebSocketMessage;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
public class SimpleWebSocketMessage implements WebSocketMessage<String> {

    String payload;

    int payloadLength;

    boolean last;

    public SimpleWebSocketMessage(String payload, boolean last) {
        this.payload = payload;
        this.payloadLength = payload.length();
        this.last = last;
    }
}
