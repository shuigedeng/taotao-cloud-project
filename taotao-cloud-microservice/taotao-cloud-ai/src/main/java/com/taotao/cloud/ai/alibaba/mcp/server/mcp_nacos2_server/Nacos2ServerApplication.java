package com.taotao.cloud.ai.alibaba.mcp.server.mcp_nacos2_server;

import com.alibaba.cloud.ai.autoconfigure.mcp.server.Nacos2McpGatewayServerAutoConfiguration;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author yingzi
 * @date 2025/5/31 17:18
 */
@SpringBootApplication(exclude = Nacos2McpGatewayServerAutoConfiguration.class)
public class Nacos2ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Nacos2ServerApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider timeTools(TimeService timeService) {
        return MethodToolCallbackProvider.builder().toolObjects(timeService).build();
    }
}
