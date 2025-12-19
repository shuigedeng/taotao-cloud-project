package com.taotao.cloud.message.biz.channels.netty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * NettyConfig
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Configuration
@EnableConfigurationProperties
public class NettyConfig {

    @Autowired
    HoleNettyProperties holeNettyProperties;

    /**
     * boss 线程池 负责客户端连接
     */
    @Bean
    public NioEventLoopGroup boosGroup() {
        return new NioEventLoopGroup(holeNettyProperties.getBoss());
    }

    /**
     * worker线程池 负责业务处理
     */
    @Bean
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(holeNettyProperties.getWorker());
    }

    /**
     * 服务器启动器
     */
    @Bean
    public ServerBootstrap serverBootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup(), workerGroup())   // 指定使用的线程组
                .channel(NioServerSocketChannel.class) // 指定使用的通道
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, holeNettyProperties.getTimeout()) // 指定连接超时时间
                .childHandler(new ServerHandler()); // 指定worker处理器
        return serverBootstrap;
    }
}

