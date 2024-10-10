package vnavesnoj.status_ping_controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import vnavesnoj.status_ping_controller.config.ControllerConfiguration;
import vnavesnoj.status_ping_controller.config.TestContainersConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(classes = ControllerConfiguration.class)
@Import(TestContainersConfiguration.class)
@Sql(scripts = {
        "classpath:test-schema.sql",
        "classpath:test-data.sql"
})
public @interface IT {
}
