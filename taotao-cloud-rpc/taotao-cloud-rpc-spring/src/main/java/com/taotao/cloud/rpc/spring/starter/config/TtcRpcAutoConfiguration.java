package com.taotao.cloud.rpc.spring.starter.config;

import com.taotao.cloud.rpc.common.api.Protocol;
import com.taotao.cloud.rpc.common.spi.ExtensionLoader;
import com.taotao.cloud.rpc.spring.starter.properties.TtcRpcProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * ttc-RPC 自动装配入口
 */
@Configuration
@ConditionalOnProperty(prefix = "ttc.rpc", name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(TtcRpcProperties.class)
@Import({TtcRpcServerAutoConfiguration.class, TtcRpcConsumerAutoConfiguration.class, TtcRpcRegistryAutoConfiguration.class, TtcRpcServiceExporter.class, TtcRpcReferenceInjector.class})
public class TtcRpcAutoConfiguration {

    private static final Logger log = LogManager.getLogger(TtcRpcAutoConfiguration.class);

    public TtcRpcAutoConfiguration() {
        log.info("ttc-RPC framework initialized");
        // 验证 SPI 加载
        try {
            Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getDefaultExtension();
            log.info("Default Protocol SPI loaded: {}", protocol.getClass().getName());
        } catch (Exception e) {
            log.warn("Protocol SPI not loaded: {}", e.getMessage());
        }
    }

    @Bean
    public TtcRpcFrameworkInitializer ttcrpcFrameworkInitializer(TtcRpcProperties properties) {
        return new TtcRpcFrameworkInitializer(properties);
    }
}
