package vnavesnoj.status_ping_controller.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vnavesnoj.status_ping_controller.dto.Status;

import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponsePayload {

    private String nickname;

    private Status status;

    private Instant instant;
}
