package vnavesnoj.status_ping_controller.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import vnavesnoj.status_ping_controller.dto.Status;
import vnavesnoj.status_ping_controller.dto.UserRequestPayload;
import vnavesnoj.status_ping_controller.holder.WebSocketSessionsHolder;
import vnavesnoj.status_ping_service.service.UserService;

import java.util.Map;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> activeSessions;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public WebSocketHandler(WebSocketSessionsHolder<String> sessionHolder,
                            UserService userService,
                            ObjectMapper objectMapper) {
        this.activeSessions = sessionHolder.getSessions();
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("web socket connection established: %s".formatted(session));
        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Handle message from web socket session. Session: %s. Message: %s".formatted(session, message));
        try {
            final var payload = objectMapper.readValue(message.getPayload(), UserRequestPayload.class);
            System.out.println(payload);
            if (payload.getStatus() == Status.ONLINE) {
                Optional.of(payload)
                        .map(UserRequestPayload::getNickname)
                        .map(item -> activeSessions.put(payload.getNickname(), session))
                        .ifPresentOrElse(
                                item -> activeSessions.put(payload.getNickname(), session),
                                () -> {
                                    throw new NullPointerException(
                                            UserRequestPayload.class.getCanonicalName() + ".nickname is null"
                                    );
                                });
                log.info("Added new session connection. Key: %s. Session: %s".formatted(payload.getNickname(), session));
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}
