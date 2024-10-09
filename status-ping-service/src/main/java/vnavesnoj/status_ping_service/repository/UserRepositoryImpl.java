package vnavesnoj.status_ping_service.repository;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import vnavesnoj.status_ping_service.entity.UserEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@Component
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final String findAllByNicknameCommand;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate,
                              @Value("${app.sql.findAllByNickname.path}")
                              String commandPath,
                              @Autowired ResourceLoader resourceLoader) {
        this.jdbcTemplate = jdbcTemplate;
        final var resource = resourceLoader.getResource("file:" + commandPath);
        try {
            findAllByNicknameCommand = resource.getContentAsString(StandardCharsets.UTF_8);
            log.info("findAllByNicknameCommand is set from file: %s".formatted(commandPath));
            log.debug("findAllByNicknameCommand:\n%s".formatted(findAllByNicknameCommand));
        } catch (IOException e) {
            throw log.throwing(new RuntimeException(e));
        }
    }


    @Override
    public UserEntity findAllConnectionsByUserNickname(String nickname) {
        log.debug(UserRepositoryImpl.class.getCanonicalName() + ": findAllConnectionByUserNickname(%s)".formatted(nickname));
        return null;
    }
}
