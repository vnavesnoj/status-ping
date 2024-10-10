package vnavesnoj.status_ping_controller.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Component
public class WebSocketProperties {

    private final String[] endpoints;
    @Getter
    private final String allowedOrigins;

    public WebSocketProperties(@Value("${app.websocket.endpoints}") String[] endpoints,
                               @Value("${app.websocket.allowed-origins}") String allowedOrigins) {
        this.endpoints = endpoints;
        this.allowedOrigins = allowedOrigins;
    }

    public String[] getEndpoints() {
        return Arrays.copyOf(endpoints, endpoints.length);
    }
}
