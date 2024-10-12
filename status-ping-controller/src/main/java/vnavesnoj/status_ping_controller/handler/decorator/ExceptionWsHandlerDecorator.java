package vnavesnoj.status_ping_controller.handler.decorator;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import vnavesnoj.status_ping_controller.exception.WsMessageRequestException;
import vnavesnoj.status_ping_controller.handler.component.WsNotifier;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
public class ExceptionWsHandlerDecorator extends WebSocketHandlerDecorator {

    private final WsNotifier wsNotifier;

    public ExceptionWsHandlerDecorator(WebSocketHandler delegate,
                                       WsNotifier wsNotifier) {
        super(delegate);
        this.wsNotifier = wsNotifier;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        try {
            super.handleMessage(session, message);
        } catch (WsMessageRequestException e) {
            log.warn("Exception when handle message: %s".formatted(e));
            wsNotifier.notifySessionAboutError(session, e, HttpStatus.BAD_REQUEST);
        } catch (Throwable e) {
            log.error("Exception when handle message: %s".formatted(e));
            wsNotifier.notifySessionAboutError(session, e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
