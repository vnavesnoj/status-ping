package vnavesnoj.status_ping_controller.holder;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Component
public class WebSocketSessionHolderImpl implements WebSocketSessionsHolder<String> {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public Map<String, WebSocketSession> getSessions() {
        return sessions;
    }
}
