package vnavesnoj.status_ping_controller.holder;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@Component
public class WebSocketSessionHolderImpl implements WebSocketSessionsHolder<String> {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public Map<String, WebSocketSession> getSessions() {
        return sessions;
    }

    @Scheduled(fixedDelay = 60_000)
    @Override
    public void updateSessionConnections() {
        log.info("updateSessionConnections() started");
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            if (!entry.getValue().isOpen()) {
                try {
                    entry.getValue().close();
                } catch (IOException e) {
                    log.warn("Exception when session close. %s".formatted(e));
                }
                sessions.remove(entry.getKey());
                log.info("updateSessionConnections(): session %s removed");
            }
        }
        log.info("updateSessionConnections() finished");
    }
}
