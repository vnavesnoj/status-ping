package vnavesnoj.status_ping_controller.dto;

import lombok.Value;

import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
public class ErrorResponsePayload {

    String error;

    Instant instant;
}
