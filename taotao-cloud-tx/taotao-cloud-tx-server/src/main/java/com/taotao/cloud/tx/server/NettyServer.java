/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.tx.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;

// Netty服务端 - 事务管理者
/**
 * NettyServer
 *
 * @author shuigedeng
 * @version 2026.04
 * @since 2025-12-19 09:30:45
 */
public class NettyServer {

    // 启动类
    private final ServerBootstrap bootstrap = new ServerBootstrap();
    // NIO事件循环组
	// BossGroup：专门处理连接请求，线程数通常为1（足够应对万级连接）
	MultiThreadIoEventLoopGroup bossGroup =
		new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());

	// WorkerGroup：处理IO读写，线程数 = CPU核心数 * 2
	int workerThreads = Runtime.getRuntime().availableProcessors() * 2;
	MultiThreadIoEventLoopGroup workerGroup =
		new MultiThreadIoEventLoopGroup(workerThreads, NioIoHandler.newFactory());

    // 启动方法
    public void start( String host, int port ) {
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
    private void init() {
        bootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                // 添加一个自定义的处理器
                .childHandler(new ServerInitializer());
    }

    // 关闭方法
    public void close() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
        bootstrap.clone();
    }
}
