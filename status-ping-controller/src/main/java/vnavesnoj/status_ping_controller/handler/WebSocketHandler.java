package vnavesnoj.status_ping_controller.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.security.auth.UserPrincipal;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import vnavesnoj.status_ping_controller.dto.PrincipalResponsePayload;
import vnavesnoj.status_ping_controller.dto.Status;
import vnavesnoj.status_ping_controller.dto.UserRequestPayload;
import vnavesnoj.status_ping_controller.dto.UserResponsePayload;
import vnavesnoj.status_ping_controller.holder.WebSocketSessionsHolder;
import vnavesnoj.status_ping_service.dto.UserReadDto;
import vnavesnoj.status_ping_service.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
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

    private final String decoratedSessionAttribute = "decoratedSession";

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
        log.debug("Handle message from web socket session. Session: %s. Message: %s".formatted(session, message));
        try {
            final var payload = objectMapper.readValue(message.getPayload(), UserRequestPayload.class);
            if (payload.getPrincipal() != null && !payload.getPrincipal().isBlank() && payload.getStatus() != null) {
                session = registerNewSession(session, payload);
                notifyUserAboutSuccessSessionRegister(session);
                notifyAllUserConnectionsAboutUserStatus(payload.getPrincipal(), payload.getStatus());
            }

        } catch (Exception e) {
            log.error(e);
        }
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        try {
            session.close();
        } catch (IOException e) {
            log.error("Cannot close session on afterConnectionClosed ", e);
        }
        Optional.of(session)
                .map(item -> item.getAttributes().get(decoratedSessionAttribute))
                .map(item -> (WebSocketSession) item)
                .map(WebSocketSession::getPrincipal)
                .map(Principal::getName)
                .ifPresent(activeSessions::remove);
    }

    private void notifyUserAboutSuccessSessionRegister(WebSocketSession session) throws IOException {
        final var principal = Optional.ofNullable(session.getPrincipal())
                .map(Principal::getName)
                .orElseThrow(NullPointerException::new);
        final var message = new TextMessage(objectMapper.writeValueAsString(
                new PrincipalResponsePayload(session.getId(), principal, Instant.now())
        ));
        session.sendMessage(message);
        log.debug("Notify session %s about success session register".formatted(session));
    }

    private void notifyAllUserConnectionsAboutUserStatus(@NonNull String nickname, @NonNull Status status) {
        for (UserReadDto user : userService.findAllConnectionsByUserNickname(nickname)) {
            try {
                notifyAboutUserStatus(nickname, user, status);
            } catch (Exception e) {
                log.error("Exception, when sending response to '%s' about '%s' status '%s': %s"
                        .formatted(user.getNickname(), nickname, status, e));
            }
        }
    }

    private void notifyAboutUserStatus(@NonNull String nickname, @NonNull UserReadDto user, Status status) throws IOException {
        final var session = activeSessions.get(user.getNickname());
        if (session != null && session.isOpen()) {
            final var responsePayload = objectMapper.writeValueAsString(
                    new UserResponsePayload(nickname, status, Instant.now())
            );
            final var message = new TextMessage(responsePayload);
            session.sendMessage(message);
            log.info("Send new message to session %s. Text message: %s".formatted(session, message));
        }
    }

    private WebSocketSession registerNewSession(WebSocketSession session, UserRequestPayload payload) {
        final WebSocketSession updatedSession = session.getPrincipal() == null
                || !payload.getPrincipal().equals(session.getPrincipal().getName())
                ? new PrincipalWsSessionDecorator(session, new UserPrincipal(payload.getPrincipal()))
                : session;
        Optional.of(payload)
                .map(UserRequestPayload::getPrincipal)
                .ifPresentOrElse(
                        item -> activeSessions.put(payload.getPrincipal(), updatedSession),
                        () -> {
                            throw new NullPointerException(
                                    UserRequestPayload.class.getCanonicalName() + ".nickname is null"
                            );
                        });
        log.info("Added new session connection. Key: %s. Session: %s".formatted(payload.getPrincipal(), session));
        updatedSession.getAttributes().put(decoratedSessionAttribute, updatedSession);
        return updatedSession;
    }
}
