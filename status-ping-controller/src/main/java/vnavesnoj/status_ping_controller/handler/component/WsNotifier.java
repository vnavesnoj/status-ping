package vnavesnoj.status_ping_controller.handler.component;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.socket.WebSocketSession;
import vnavesnoj.status_ping_controller.dto.Status;
import vnavesnoj.status_ping_service.dto.UserReadDto;

import java.io.IOException;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface WsNotifier {

    void notifyUserAboutSuccessSessionRegister(WebSocketSession session) throws IOException;

    void notifyAboutUserStatus(@NonNull String nickname, @NonNull UserReadDto user, Status status) throws IOException;

    void notifyAllUserConnectionsAboutUserStatus(@NonNull String nickname, @NonNull Status status);

    void notifySessionAboutError(WebSocketSession session, Throwable throwable, HttpStatus httpStatus) throws IOException;
}
