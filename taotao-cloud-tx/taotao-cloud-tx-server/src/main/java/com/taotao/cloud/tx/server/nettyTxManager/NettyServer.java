package com.taotao.cloud.tx.server.nettyTxManager;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

// Netty服务端 - 事务管理者
public class NettyServer {
    // 启动类
    private ServerBootstrap bootstrap = new ServerBootstrap();
    // NIO事件循环组
    private NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

    // 启动方法
    public void start(String host, int port) {
        try {
            // 调用下面的初始化方法
            init();
            // 绑定端口和IP
            bootstrap.bind(host, port).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 初始化方法
    private void init(){
        bootstrap.group(nioEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                // 添加一个自定义的处理器
                .childHandler(new ServerInitializer());
    }

    // 关闭方法
    public void close(){
        nioEventLoopGroup.shutdownGracefully();
        bootstrap.clone();
    }
}
