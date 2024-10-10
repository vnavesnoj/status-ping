package vnavesnoj.status_ping_controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestPayload {

    @JsonProperty(required = true, value = "nickname")
    private String nickname;

    @JsonProperty(required = true, value = "status")
    private Status status;
}
