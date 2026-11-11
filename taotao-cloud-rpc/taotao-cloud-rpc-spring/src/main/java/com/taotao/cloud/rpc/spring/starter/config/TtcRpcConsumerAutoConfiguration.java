package com.taotao.cloud.rpc.spring.starter.config;

import com.taotao.cloud.rpc.spring.starter.properties.TtcRpcProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 消费端自动装配
 */
@Configuration
public class TtcRpcConsumerAutoConfiguration {

    public TtcRpcConsumerAutoConfiguration(TtcRpcProperties properties) {
        // 这里会在 BeanPostProcessor 中扫描 @TtcRpcReference 注解
    }
}
