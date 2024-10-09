package vnavesnoj.status_ping_service.dto;

import lombok.Value;

import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Value
public class UserReadDto {

    String nickname;

    Instant instant;
}
