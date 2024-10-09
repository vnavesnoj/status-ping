package vnavesnoj.status_ping_service.mapper;

import org.springframework.stereotype.Component;
import vnavesnoj.status_ping_service.dto.UserReadDto;
import vnavesnoj.status_ping_service.entity.UserEntity;

import java.time.Instant;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Component
public class UserReadMapper implements Mapper<UserEntity, UserReadDto> {

    @Override
    public UserReadDto map(UserEntity object) {
        return new UserReadDto(object.getNickname(), Instant.now());
    }

    @Override
    public UserReadDto map(UserEntity from, UserReadDto to) {
        return null;
    }
}
