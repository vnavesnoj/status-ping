package vnavesnoj.status_ping_controller.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import vnavesnoj.status_ping_controller.dto.Status;
import vnavesnoj.status_ping_controller.dto.UserRequestPayload;
import vnavesnoj.status_ping_controller.exception.WsMessageRequestException;
import vnavesnoj.status_ping_controller.handler.component.SessionRegistrar;
import vnavesnoj.status_ping_controller.handler.component.WsNotifier;
import vnavesnoj.status_ping_controller.holder.WebSocketSessionsHolder;

import java.io.IOException;
import java.security.Principal;
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
    private final ObjectMapper objectMapper;
    private final SessionRegistrar sessionRegistrar;
    private final WsNotifier wsNotifier;

    private final String decoratedSessionAttribute = "decoratedSession";

    public WebSocketHandler(WebSocketSessionsHolder<String> sessionHolder,
                            ObjectMapper objectMapper,
                            SessionRegistrar sessionRegistrar,
                            WsNotifier wsNotifier) {
        this.activeSessions = sessionHolder.getSessions();
        this.objectMapper = objectMapper;
        this.sessionRegistrar = sessionRegistrar;
        this.wsNotifier = wsNotifier;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("web socket connection established: %s".formatted(session));
        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.debug("Handle message from web socket session. Session: %s. Message: %s".formatted(session, message));
        try {
            final var payload = objectMapper.readValue(message.getPayload(), UserRequestPayload.class);
            if (payload.getPrincipal() != null && !payload.getPrincipal().isBlank() && payload.getStatus() != null) {
                session = sessionRegistrar.registerNewSession(session, payload);
                wsNotifier.notifyUserAboutSuccessSessionRegister(session);
                wsNotifier.notifyAllUserConnectionsAboutUserStatus(payload.getPrincipal(), payload.getStatus());
            }

        } catch (JsonParseException | JsonMappingException e) {
            final var requestTemplate = objectMapper.writeValueAsString(
                    new UserRequestPayload(UserRequestPayload.Fields.principal, Status.ONLINE)
            );
            final var detail = "message request must be in JSON format: %s".formatted(requestTemplate);
            throw new WsMessageRequestException(detail, e);
        }
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        try {
            session.close();
        } catch (IOException e) {
            log.warn("Cannot close session on afterConnectionClosed ", e);
        }
        Optional.of(session)
                .map(item -> item.getAttributes().get(decoratedSessionAttribute))
                .map(item -> (WebSocketSession) item)
                .map(WebSocketSession::getPrincipal)
                .map(Principal::getName)
                .ifPresent(activeSessions::remove);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Transport error detected: %s".formatted(exception));
        log.debug(exception.getStackTrace());
        super.handleTransportError(session, exception);
    }
}
