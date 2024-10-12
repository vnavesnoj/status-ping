package vnavesnoj.status_ping_controller.dto;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
public class UserStatusResponsePayload {

    Integer statusCode = HttpStatus.CONTINUE.value();

    String user;

    Status status;

    Instant instant;
}
