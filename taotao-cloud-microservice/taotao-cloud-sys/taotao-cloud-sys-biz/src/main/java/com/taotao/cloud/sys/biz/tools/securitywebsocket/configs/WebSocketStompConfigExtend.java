package com.taotao.cloud.sys.biz.tools.securitywebsocket.configs;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfigExtend implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private AuthChannelInterceptor authChannelInterceptor;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authChannelInterceptor);

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors() * 2);
        taskExecutor.setAllowCoreThreadTimeOut(true);
        taskExecutor.setTaskDecorator(new AsyncTaskDecorator());
        registration.taskExecutor(taskExecutor);
    }

    /**
     * 对于异步任务时, 同样也能获取到 TraceId 和权限信息
     * spring 的异步任务 @Async
     */
    public static class AsyncTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            try {
                RequestAttributes context = RequestContextHolder.currentRequestAttributes();
                Map<String,String> previous = MDC.getCopyOfContextMap();
                final SecurityContext securityContext = SecurityContextHolder.getContext();
                return () -> {
                    try {
                        RequestContextHolder.setRequestAttributes(context);

                        SecurityContextHolder.setContext(securityContext);

                        MDC.setContextMap(previous);

                        runnable.run();
                    } finally {
                        RequestContextHolder.resetRequestAttributes();

                        SecurityContextHolder.clearContext();

                        MDC.clear();
                    }
                };
            } catch (IllegalStateException e) {
                return runnable;
            }
        }
    }
}
