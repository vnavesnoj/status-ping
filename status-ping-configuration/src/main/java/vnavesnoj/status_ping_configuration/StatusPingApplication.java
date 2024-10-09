package vnavesnoj.status_ping_configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@SpringBootApplication(scanBasePackages = {
        "vnavesnoj.status_ping_service"
})
public class StatusPingApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatusPingApplication.class, args);
    }
}
