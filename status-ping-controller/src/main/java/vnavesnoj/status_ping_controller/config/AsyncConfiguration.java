package vnavesnoj.status_ping_controller.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author vnavesnoj
 * @mail vnavesnoj@gmail.com
 */

@Log4j2
@EnableAsync
@Configuration
@ConditionalOnProperty(name = "app.async.enabled", matchIfMissing = true)
public class AsyncConfiguration implements AsyncConfigurer {

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.error("exception from method " + method.getName(), ex);
    }
}
