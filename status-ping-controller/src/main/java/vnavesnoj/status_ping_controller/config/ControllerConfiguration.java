package vnavesnoj.status_ping_controller.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Configuration
@ComponentScan(basePackages = {"vnavesnoj.status_ping_controller", "vnavesnoj.status_ping_service"})
@EnableAutoConfiguration
public class ControllerConfiguration {
}
