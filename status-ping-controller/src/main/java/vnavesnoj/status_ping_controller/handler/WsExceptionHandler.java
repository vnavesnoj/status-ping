package vnavesnoj.status_ping_controller.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import vnavesnoj.status_ping_controller.dto.ErrorResponsePayload;
import vnavesnoj.status_ping_controller.exception.WsMessageRequestException;

import java.io.IOException;
import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@Aspect
@Component
public class WsExceptionHandler {

    private final ObjectMapper objectMapper;

    @Pointcut("execution(void WebSocketHandler.handleTextMessage(" +
            "org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.TextMessage" +
            "))")
    void isHandleMessageTextMethod() {
    }

    @AfterThrowing(pointcut = "isHandleMessageTextMethod()", throwing = "exception", argNames = "exception,joinPoint")
    void handleException(Throwable exception, JoinPoint joinPoint) {
        try {
            final var wsSession = (WebSocketSession) joinPoint.getArgs()[0];
            if (exception instanceof WsMessageRequestException) handleWsMessageRequestException(exception, wsSession);
        } catch (Throwable e) {
            log.error("Exception when handle web socket message exception: %s".formatted(e));
        }
    }

    private void handleWsMessageRequestException(Throwable exception, WebSocketSession session) throws IOException {
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
