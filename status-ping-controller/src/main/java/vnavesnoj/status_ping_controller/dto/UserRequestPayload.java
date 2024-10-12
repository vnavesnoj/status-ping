package vnavesnoj.status_ping_controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class UserRequestPayload {

    @JsonProperty(required = true, value = "principal")
    private String principal;

    @JsonProperty(required = true, value = "status")
    private Status status;
}
