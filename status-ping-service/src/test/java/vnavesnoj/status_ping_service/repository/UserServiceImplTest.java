package vnavesnoj.status_ping_service.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import vnavesnoj.status_ping_service.IT;
import vnavesnoj.status_ping_service.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@IT
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void findAllConnectionsByUserNickname() {
        final var result1 = userService.findAllConnectionsByUserNickname("dummy1");
        assertThat(result1)
                .isNotEmpty()
                .hasSize(3);

        final var result2 = userService.findAllConnectionsByUserNickname("dummy2");
        assertThat(result2)
                .isNotEmpty()
                .hasSize(2);

        final var result3 = userService.findAllConnectionsByUserNickname("dummy5");
        assertThat(result3).isEmpty();
    }
}
