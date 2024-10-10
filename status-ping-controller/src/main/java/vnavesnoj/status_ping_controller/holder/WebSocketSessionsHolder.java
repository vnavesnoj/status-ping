package vnavesnoj.status_ping_controller.holder;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface WebSocketSessionsHolder<T> {

    Map<T, WebSocketSession> getSessions();
}
