package com.taotao.cloud.sys.biz.tools.console.configs;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {
    @Override
    @Bean(name = "asyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(100);
        executor.setThreadGroupName("MyCustomExecutor");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setBeanName("asyncExecutor");
        executor.setTaskDecorator(new AsyncTaskDecorator());
//        executor.initialize();

        return executor;
    }

    /**
     * 对于异步任务时, 同样也能获取到 TraceId
     * spring 的异步任务 @Async
     */
    public static class AsyncTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            try {
                RequestAttributes context = RequestContextHolder.currentRequestAttributes();
                Map<String,String> previous = MDC.getCopyOfContextMap();
                return () -> {
                    try {
                        RequestContextHolder.setRequestAttributes(context);

                        MDC.setContextMap(previous);

                        runnable.run();
                    } finally {
                        RequestContextHolder.resetRequestAttributes();
                        MDC.clear();
                    }
                };
            } catch (IllegalStateException e) {
                return runnable;
            }
        }
    }
}
