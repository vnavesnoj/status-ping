package vnavesnoj.status_ping_service.service;

import vnavesnoj.status_ping_service.dto.UserReadDto;

import java.util.List;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface UserService {

    List<UserReadDto> findAllConnectionsByUserNickname(String nickname);
}
