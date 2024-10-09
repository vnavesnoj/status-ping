package vnavesnoj.status_ping_service.repository;

import vnavesnoj.status_ping_service.entity.UserEntity;

import java.util.List;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
public interface UserRepository {

    List<UserEntity> findAllConnectionsByUserNickname(String nickname);
}
