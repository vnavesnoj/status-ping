package vnavesnoj.status_ping_controller.handler.decorator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import vnavesnoj.status_ping_controller.dto.ErrorResponsePayload;
import vnavesnoj.status_ping_controller.exception.WsMessageRequestException;

import java.io.IOException;
import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
public class ExceptionWsHandlerDecorator extends WebSocketHandlerDecorator {

    private final ObjectMapper objectMapper;

    public ExceptionWsHandlerDecorator(WebSocketHandler delegate, ObjectMapper objectMapper) {
        super(delegate);
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        try {
            super.handleMessage(session, message);
        } catch (WsMessageRequestException e) {
            handleWsMessageRequestException(e, session);
        } catch (Throwable e) {
            log.error("Exception when handle web socket message exception: %s".formatted(e));
        }
    }

    private void handleWsMessageRequestException(WsMessageRequestException exception, WebSocketSession session) throws IOException {
        final var badMessageRequest = new ErrorResponsePayload(
                HttpStatus.BAD_REQUEST.value(),
                "bad message request",
                exception.getMessage(),
                Instant.now()
        );
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(badMessageRequest)));
        }
    }
}
