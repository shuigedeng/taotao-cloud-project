package com.taotao.cloud.iot.biz.infrastructure.communication.tcp.config;

import com.taotao.cloud.iot.biz.infrastructure.communication.tcp.factory.TcpMessageHandlerFactory;
import com.taotao.cloud.iot.biz.infrastructure.communication.tcp.handler.ConnectionHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *  Netty服务配置
 *
 * @author 
 */
@Configuration
@Slf4j
public class NettyServerConfig {

    @Bean
    public ConcurrentMap<String, Channel> deviceChannels() {
        return new ConcurrentHashMap<>();
    }

    @Autowired
    public TcpMessageHandlerFactory tcpMessageHandlerFactory;

    @Bean
    public ServerBootstrap nettyServer(ConcurrentMap<String, Channel> deviceChannels) {
        ServerBootstrap bootstrap = new ServerBootstrap();
		// NIO事件循环组
		// BossGroup：专门处理连接请求，线程数通常为1（足够应对万级连接）
		MultiThreadIoEventLoopGroup bossGroup =
			new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());

		// WorkerGroup：处理IO读写，线程数 = CPU核心数 * 2
		int workerThreads = Runtime.getRuntime().availableProcessors() * 2;
		MultiThreadIoEventLoopGroup workerGroup =
			new MultiThreadIoEventLoopGroup(workerThreads, NioIoHandler.newFactory());

		bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(
                                new StringDecoder(),
                                new StringEncoder(),
                                // 添加设备连接处理器
                                new ConnectionHandler(deviceChannels,tcpMessageHandlerFactory)
                        );
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        return bootstrap;
    }

    @Bean
    public ChannelFuture serverChannelFuture(ServerBootstrap serverBootstrap) throws InterruptedException {
        try {
            ChannelFuture future = serverBootstrap.bind(8888).sync();
            log.info("------------------------ Netty 服务器在端口 8888 启动成功");
            return future;
        } catch (Exception e) {
            log.error("------------------------ Netty 服务器启动失败", e);
            throw e;
        }
    }
}
