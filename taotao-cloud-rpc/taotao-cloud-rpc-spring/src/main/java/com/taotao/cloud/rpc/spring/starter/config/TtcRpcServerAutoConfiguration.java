package com.taotao.cloud.rpc.spring.starter.config;

import com.taotao.cloud.rpc.remoting.RpcServer;
import com.taotao.cloud.rpc.spring.starter.properties.TtcRpcProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * 服务端自动装配
 *
 * 在 Spring 上下文启动完成后，启动 Netty RPC Server。
 */
@Configuration
public class TtcRpcServerAutoConfiguration {

    private static final Logger log = LogManager.getLogger(TtcRpcServerAutoConfiguration.class);

    @Autowired
    private TtcRpcProperties properties;

    @Bean(destroyMethod = "stop")
    public RpcServer rpcServer() {
        // host 原样交给 RpcServer 绑定：0.0.0.0 的语义就是"所有网卡"。
        // 这里曾把 0.0.0.0 换成 InetAddress.getLocalHost()，而 Debian/Ubuntu 上它返回
        // 127.0.1.1 —— 服务端只监听这一个回环别名，本机的 127.0.0.1 和外部机器都连不上。
        return new RpcServer(properties.getServer().getHost(), properties.getServer().getPort());
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshed() {
        if (!properties.getServer().isEnabled()) {
            return;
        }
        try {
            rpcServer().start(true);
            log.info("[TtcRpcServer] Netty RPC server started at {}:{}",
                    rpcServer().getHost(), rpcServer().getPort());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Failed to start RPC server", e);
        }
    }
}
