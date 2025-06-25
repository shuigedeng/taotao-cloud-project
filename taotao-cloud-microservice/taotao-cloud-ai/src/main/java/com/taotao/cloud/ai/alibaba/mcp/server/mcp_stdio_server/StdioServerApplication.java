package com.taotao.cloud.ai.alibaba.mcp.server.mcp_stdio_server;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author yingzi
 * @date 2025/5/28 09:25
 */
@SpringBootApplication
public class StdioServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StdioServerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider timeTools(TimeService timeService) {
        return MethodToolCallbackProvider.builder().toolObjects(timeService).build();
    }
}
