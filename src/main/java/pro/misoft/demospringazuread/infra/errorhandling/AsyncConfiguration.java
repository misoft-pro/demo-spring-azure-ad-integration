package pro.misoft.demospringazuread.infra.errorhandling;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Executor;


@Configuration
public class AsyncConfiguration extends AsyncConfigurerSupport {

    private static final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("async-thread-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setTaskDecorator(new AsyncTaskDecorator());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.info(String.format("Exception in method %s", method.getName()), ex);
    }

    private static class AsyncTaskDecorator implements TaskDecorator {
        @NotNull
        @Override
        public Runnable decorate(@NotNull Runnable runnable) {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            if (contextMap == null) {
                return runnable;
            }
            return () -> {
                try {
                    MDC.setContextMap(contextMap);
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        }
    }
}