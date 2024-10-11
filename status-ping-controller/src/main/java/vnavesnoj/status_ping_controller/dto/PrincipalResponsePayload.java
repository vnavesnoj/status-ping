package vnavesnoj.status_ping_controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrincipalResponsePayload {

    private String id;

    private String principal;

    private Instant instant;
}
