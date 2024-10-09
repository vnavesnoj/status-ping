package vnavesnoj.status_ping_service.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import vnavesnoj.status_ping_service.IT;
import vnavesnoj.status_ping_service.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@RequiredArgsConstructor
@IT
class UserRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final UserEntity dummy1 = new UserEntity("dummy1");
    private final UserEntity dummy2 = new UserEntity("dummy2");
    private final UserEntity dummy3 = new UserEntity("dummy3");
    private final UserEntity dummy4 = new UserEntity("dummy4");
    private final UserEntity dummy5 = new UserEntity("dummy5");

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAllConnectionsByUserNickname() {
        final var result1 = userRepository.findAllConnectionsByUserNickname("dummy1");
        assertThat(result1)
                .isNotEmpty()
                .hasSize(3)
                .containsExactly(dummy2, dummy3, dummy4);

        final var result2 = userRepository.findAllConnectionsByUserNickname("dummy2");
        assertThat(result2)
                .isNotEmpty()
                .hasSize(2)
                .containsExactly(dummy3, dummy4);

        final var result3 = userRepository.findAllConnectionsByUserNickname("dummy5");
        assertThat(result3).isEmpty();
    }
}