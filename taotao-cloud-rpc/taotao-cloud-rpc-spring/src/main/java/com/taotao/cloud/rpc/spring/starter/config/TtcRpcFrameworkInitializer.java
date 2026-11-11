package com.taotao.cloud.rpc.spring.starter.config;

import com.taotao.cloud.rpc.spring.starter.properties.TtcRpcProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ttc-RPC 框架初始化器
 * <p>
 * 启动时打印框架配置信息，验证 SPI 加载。
 */
public class TtcRpcFrameworkInitializer {

    private static final Logger log = LogManager.getLogger(TtcRpcFrameworkInitializer.class);

    public TtcRpcFrameworkInitializer(TtcRpcProperties properties) {
        log.info("===========================================");
        log.info("ttc-RPC Framework Configuration:");
        log.info("  Application: {} v{}", properties.getApplication().getName(), properties.getApplication().getVersion());
        log.info("  Organization: {}", properties.getApplication().getOrganization());
        log.info("  Registry: {} ({})", properties.getRegistry().getType(), properties.getRegistry().getAddress());
        log.info("  Server: {}:{}", properties.getServer().getHost(), properties.getServer().getPort());
        log.info("  Protocol: {}, Serialization: {}",
                properties.getProtocol().getName(), properties.getProtocol().getSerialization());
        log.info("  Consumer: cluster={}, loadbalance={}, timeout={}ms",
                properties.getConsumer().getCluster(), properties.getConsumer().getLoadbalance(),
                properties.getConsumer().getTimeout());
        log.info("===========================================");
    }
}
