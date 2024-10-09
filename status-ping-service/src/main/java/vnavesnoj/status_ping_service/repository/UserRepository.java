package vnavesnoj.status_ping_service.repository;

import vnavesnoj.status_ping_service.entity.UserEntity;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface UserRepository {

    UserEntity findAllConnectionsByUserNickname(String nickname);
}
