package vnavesnoj.status_ping_controller.handler.decorator;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketSessionDecorator;

import java.security.Principal;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public class PrincipalWsSessionDecorator extends WebSocketSessionDecorator {

    private final Principal principal;

    public PrincipalWsSessionDecorator(WebSocketSession session, Principal principal) {
        super(session);
        this.principal = principal;
    }

    @Override
    public Principal getPrincipal() {
        return principal;
    }
}
