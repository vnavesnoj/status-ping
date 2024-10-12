package vnavesnoj.status_ping_controller.handler.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import vnavesnoj.status_ping_controller.dto.ErrorResponsePayload;
import vnavesnoj.status_ping_controller.dto.PrincipalResponsePayload;
import vnavesnoj.status_ping_controller.dto.Status;
import vnavesnoj.status_ping_controller.dto.UserStatusResponsePayload;
import vnavesnoj.status_ping_controller.holder.WebSocketSessionsHolder;
import vnavesnoj.status_ping_service.dto.UserReadDto;
import vnavesnoj.status_ping_service.service.UserService;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class WsNotifierImpl implements WsNotifier {

    private final ObjectMapper objectMapper;
    private final WebSocketSessionsHolder<String> sessionsHolder;
    private final UserService userService;

    public void notifyUserAboutSuccessSessionRegister(WebSocketSession session) throws IOException {
        final var principal = Optional.ofNullable(session.getPrincipal())
                .map(Principal::getName)
                .orElseThrow(NullPointerException::new);
        final var message = new TextMessage(objectMapper.writeValueAsString(
                new PrincipalResponsePayload(session.getId(), principal, Instant.now())
        ));
        session.sendMessage(message);
        log.debug("Notify session %s about success session register".formatted(session));
    }

    public void notifyAboutUserStatus(@NonNull String nickname, @NonNull UserReadDto user, Status status) throws IOException {
        final var session = sessionsHolder.getSessions().get(user.getNickname());
        if (session != null && session.isOpen()) {
            final var responsePayload = objectMapper.writeValueAsString(
                    new UserStatusResponsePayload(nickname, status, Instant.now())
            );
            final var message = new TextMessage(responsePayload);
            session.sendMessage(message);
            log.info("Send new message to session %s. Text message: %s".formatted(session, message));
        }
    }

    public void notifyAllUserConnectionsAboutUserStatus(@NonNull String nickname, @NonNull Status status) {
        for (UserReadDto user : userService.findAllConnectionsByUserNickname(nickname)) {
            try {
                notifyAboutUserStatus(nickname, user, status);
            } catch (Exception e) {
                log.error("Exception, when sending response to '%s' about '%s' status '%s': %s"
                        .formatted(user.getNickname(), nickname, status, e));
            }
        }
    }

    public void notifySessionAboutError(WebSocketSession session, Throwable throwable, HttpStatus httpStatus)
            throws IOException {
        final var serverErrorRequest = new ErrorResponsePayload(
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                throwable.getMessage(),
                Instant.now()
        );
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(serverErrorRequest)));
            log.info("Send an error message to the session %s. Error: %s".formatted(session, throwable));
        } else {
            log.warn("Cannot send an error message to the session %s. Error: %s".formatted(session, throwable));
        }
    }
}