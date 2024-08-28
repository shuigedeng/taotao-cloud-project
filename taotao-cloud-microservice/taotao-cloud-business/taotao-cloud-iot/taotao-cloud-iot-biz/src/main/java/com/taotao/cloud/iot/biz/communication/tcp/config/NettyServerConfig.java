package com.taotao.cloud.iot.biz.communication.tcp.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import net.maku.iot.communication.tcp.factory.TcpMessageHandlerFactory;
import net.maku.iot.communication.tcp.handler.ConnectionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *  Netty服务配置
 *
 * @author LSF maku_lsf@163.com
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
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
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
