package vnavesnoj.status_ping_controller.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import vnavesnoj.status_ping_controller.handler.decorator.ExceptionWsHandlerDecorator;

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
    private final ObjectMapper objectMapper;
    private final WebSocketProperties webSocketProperties;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        final var endpoints = new String[]{"/status"};
        final var allowedOrigins = "*";
        registry.addHandler(
                        new ExceptionWsHandlerDecorator(webSocketHandler, objectMapper),
                        webSocketProperties.getEndpoints()
                )
                .setAllowedOrigins(webSocketProperties.getAllowedOrigins())
                .addInterceptors(new HttpSessionHandshakeInterceptor());
        log.info("Register web socket handler with endpoints: %s. Allowed origins: %s"
                .formatted(Arrays.toString(endpoints), allowedOrigins));
    }
}
