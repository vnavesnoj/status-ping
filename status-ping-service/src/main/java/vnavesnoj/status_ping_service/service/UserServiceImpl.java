package vnavesnoj.status_ping_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import vnavesnoj.status_ping_service.dto.UserReadDto;
import vnavesnoj.status_ping_service.entity.UserEntity;
import vnavesnoj.status_ping_service.mapper.Mapper;
import vnavesnoj.status_ping_service.repository.UserRepository;

import java.util.List;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Mapper<UserEntity, UserReadDto> userReadMapper;

    @Override
    public List<UserReadDto> findAllConnectionsByUserNickname(String nickname) {
        log.trace("findAllConnectionsByUserNickname(%s)".formatted(nickname));
        return userRepository.findAllConnectionsByUserNickname(nickname).stream()
                .map(userReadMapper::map)
                .toList();
    }
}
