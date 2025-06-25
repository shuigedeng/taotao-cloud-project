package com.taotao.cloud.ai.alibaba.mcp.server.mcp_webflux_server;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author yingzi
 * @date 2025/5/28 08:53
 */
@SpringBootApplication
public class WebfluxServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxServerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider timeTools(TimeService timeService) {
        return MethodToolCallbackProvider.builder().toolObjects(timeService).build();
    }
}
