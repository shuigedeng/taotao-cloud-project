/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.ws.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * NettyServer
 *
 * @author shuigedeng
 * @since 2020/12/30 下午4:35
 * @version 2022.03
 */
@Component
@Configuration
public class NettyServer {
	private final Integer port = 8773;

	final EventLoopGroup bossGroup = new NioEventLoopGroup();
	final EventLoopGroup workerGroup = new NioEventLoopGroup();

	public void run() throws Exception {
		//创建BossGroup 和 WorkerGroup
		//说明
		//1. 创建两个线程组 bossGroup 和 workerGroup
		//2. bossGroup 只是处理连接请求 , 真正的和客户端业务处理，会交给 workerGroup完成
		//3. 两个都是无限循环
		//4. bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数
		//默认实际 cpu核数 * 2
		try {
			//创建服务器端的启动对象，配置参数
			ServerBootstrap bootstrap = new ServerBootstrap();
			//使用链式编程来进行设置
			//设置两个线程组
			bootstrap.group(bossGroup, workerGroup)
				//使用NioSocketChannel 作为服务器的通道实现
				.channel(NioServerSocketChannel.class)
				// 设置线程队列得到连接个数(也可以说是并发数)
				.option(ChannelOption.SO_BACKLOG, 2048)
				//设置保持活动连接状态
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				//.handler(null) // 该 handler对应 bossGroup , childHandler 对应 workerGroup
				//.childOption(ChannelOption.TCP_NODELAY,true)//socketchannel的设置,关闭延迟发送
				.childHandler(new NettyInitializer());

			log.info(".....服务器 is ready.....");
			//绑定一个端口并且同步, 生成了一个 ChannelFuture 对象
			//启动服务器(并绑定端口)
			ChannelFuture cf = bootstrap.bind(port).sync();
			//给cf 注册监听器，监控我们关心的事件
			cf.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (cf.isSuccess()) {
						log.info("监听端口[{}]成功!", port);
					} else {
						log.error("监听端口[{}]失败!", port);
					}
				}
			});
			//对关闭通道进行监听
			cf.channel().closeFuture().sync();
		} catch (Exception e) {
			log.error(" netty服务启动异常 " + e.getMessage());
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
