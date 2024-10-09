package vnavesnoj.status_ping_service.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "vnavesnoj.status_ping_service")
public class ServiceAutoConfiguration {
}
