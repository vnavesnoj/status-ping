package vnavesnoj.status_ping_service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import vnavesnoj.status_ping_service.config.ServiceAutoConfiguration;
import vnavesnoj.status_ping_service.repository.TestContainersConfiguration;

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
@Transactional
@SpringBootTest
@ContextConfiguration(classes = ServiceAutoConfiguration.class)
@Import(TestContainersConfiguration.class)
@Sql(scripts = {
        "classpath:test-schema.sql",
        "classpath:test-data.sql"
})
public @interface IT {
}
