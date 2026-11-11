package com.taotao.cloud.rpc.spring.starter.config;

import com.taotao.cloud.rpc.spring.starter.properties.TtcRpcProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 注册中心自动装配
 */
@Configuration
public class TtcRpcRegistryAutoConfiguration {

    public TtcRpcRegistryAutoConfiguration(TtcRpcProperties properties) {
        if (properties.getRegistry().isEnabled()) {
            // 这里会初始化 ttc-config 客户端
        }
    }
}
