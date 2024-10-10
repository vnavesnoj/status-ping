package vnavesnoj.status_ping_controller.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Arrays;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        final var endpoints = new String[]{"/status"};
        final var allowedOrigins = "*";
        registry.addHandler(webSocketHandler, "/status")
                .setAllowedOrigins(allowedOrigins)
                .addInterceptors(new HttpSessionHandshakeInterceptor());
        log.info("Register web socket handler with endpoints: %s. Allowed origins: %s"
                .formatted(Arrays.toString(endpoints), allowedOrigins));
    }
}
