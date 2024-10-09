package vnavesnoj.status_ping_service.repository;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import vnavesnoj.status_ping_service.entity.UserEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Log4j2
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String findAllByNicknameCommand;

    public UserRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate,
                              @Value("${app.sql.findAllByNickname.path}")
                              String commandPath,
                              @Autowired ResourceLoader resourceLoader) {
        this.jdbcTemplate = jdbcTemplate;
        final var resource = resourceLoader.getResource(commandPath);
        try {
            findAllByNicknameCommand = resource.getContentAsString(StandardCharsets.UTF_8);
            log.info("findAllByNicknameCommand is set from file: %s".formatted(commandPath));
            log.debug("findAllByNicknameCommand:\n%s".formatted(findAllByNicknameCommand));
        } catch (IOException e) {
            throw log.throwing(new RuntimeException(e));
        }
    }


    @Override
    public List<UserEntity> findAllConnectionsByUserNickname(String nickname) {
        log.trace(UserRepositoryImpl.class.getCanonicalName() + ": findAllConnectionByUserNickname(%s)".formatted(nickname));
        final var params = new MapSqlParameterSource("nickname", nickname);
        final var result = jdbcTemplate.query(findAllByNicknameCommand, params, new SingleColumnRowMapper<>(UserEntity.class));
        log.debug("findAllConnectionByUserNickname(%s): fetched data = %s".formatted(nickname, result));
        return result;
    }
}
