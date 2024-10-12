package vnavesnoj.status_ping_controller.handler.component;

import com.sun.security.auth.UserPrincipal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import vnavesnoj.status_ping_controller.dto.UserRequestPayload;
import vnavesnoj.status_ping_controller.handler.decorator.PrincipalWsSessionDecorator;
import vnavesnoj.status_ping_controller.holder.WebSocketSessionsHolder;

import java.util.Optional;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class SessionRegistrar {

    private final WebSocketSessionsHolder<String> sessionsHolder;
    private final String decoratedSessionAttribute = "decoratedSession";

    public WebSocketSession registerNewSession(WebSocketSession session, UserRequestPayload payload) {
        final WebSocketSession updatedSession = session.getPrincipal() == null
                || !payload.getPrincipal().equals(session.getPrincipal().getName())
                ? new PrincipalWsSessionDecorator(session, new UserPrincipal(payload.getPrincipal()))
                : session;
        Optional.of(payload)
                .map(UserRequestPayload::getPrincipal)
                .ifPresentOrElse(
                        item -> sessionsHolder.getSessions().put(payload.getPrincipal(), updatedSession),
                        () -> {
                            throw new NullPointerException(
                                    UserRequestPayload.class.getCanonicalName() + ".nickname is null"
                            );
                        });
        log.info("Added new session connection. Key: %s. Session: %s".formatted(payload.getPrincipal(), session));
        updatedSession.getAttributes().put(decoratedSessionAttribute, updatedSession);
        return updatedSession;
    }

    public boolean isRegistered(@NonNull WebSocketSession session) {
        return Optional.of(session)
                .map(WebSocketSession::getAttributes)
                .map(item -> item.get(decoratedSessionAttribute))
                .isPresent();
    }
}
