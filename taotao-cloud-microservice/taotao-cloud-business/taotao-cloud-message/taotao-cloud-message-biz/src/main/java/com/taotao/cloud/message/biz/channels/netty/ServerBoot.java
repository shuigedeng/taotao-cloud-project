package com.taotao.cloud.message.biz.channels.netty;

import io.netty.bootstrap.ServerBootstrap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ServerBoot
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Component
@Slf4j
public class ServerBoot {

    @Autowired
    ServerBootstrap serverBootstrap;
    @Resource
    NioEventLoopGroup boosGroup;
    @Resource
    NioEventLoopGroup workerGroup;
    @Autowired
    HoleNettyProperties holeNettyProperties;

    /**
     * 开机启动
     */
    @PostConstruct
    public void start() throws InterruptedException {
        // 绑定端口启动
        serverBootstrap.bind(holeNettyProperties.getPort()).sync();
        serverBootstrap.bind(holeNettyProperties.getPortSalve()).sync();
        log.info("启动Netty多端口服务器: {},{}", holeNettyProperties.getPort(), holeNettyProperties.getPortSalve());
    }

    /**
     * 关闭线程池
     */
    @PreDestroy
    public void close() throws InterruptedException {
        log.info("关闭Netty服务器");
        boosGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}

