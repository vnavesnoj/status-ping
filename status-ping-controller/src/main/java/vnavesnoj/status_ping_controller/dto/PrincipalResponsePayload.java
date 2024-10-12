package vnavesnoj.status_ping_controller.dto;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
public class PrincipalResponsePayload {

    Integer statusCode = HttpStatus.OK.value();

    String id;

    String principal;

    Instant instant;
}
