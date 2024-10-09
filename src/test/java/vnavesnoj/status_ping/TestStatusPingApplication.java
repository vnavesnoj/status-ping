package vnavesnoj.status_ping;

import org.springframework.boot.SpringApplication;

public class TestStatusPingApplication {

	public static void main(String[] args) {
		SpringApplication.from(StatusPingApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
