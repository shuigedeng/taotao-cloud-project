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

package com.taotao.cloud.tx.rm.netty;

import com.alibaba.fastjson2.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NettyClient
 *
 * @author shuigedeng
 * @version 2026.04
 * @since 2025-12-19 09:30:45
 */
@Component
public class NettyClient implements InitializingBean {

	private NettyClientHandler client = null;
	private static ExecutorService executorService = Executors.newCachedThreadPool();

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("\n\n>>>>>>事务参与者启动成功<<<<<<\n\n");
		start("localhost", 8080);
	}

	// 根据IP、端口地址，向服务端注册客户端
	public void start( String host, int port ) {
		client = new NettyClientHandler();
		Bootstrap bootstrap = new Bootstrap();

		// WorkerGroup：处理IO读写，线程数 = CPU核心数 * 2
		int workerThreads = Runtime.getRuntime().availableProcessors() * 2;
		MultiThreadIoEventLoopGroup workerGroup =
			new MultiThreadIoEventLoopGroup(workerThreads, NioIoHandler.newFactory());
		bootstrap
			.group(workerGroup)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ClientInitializer(client));
		try {
			bootstrap.connect(host, port).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void send( JSONObject sendData ) {
		try {
			client.sendData(sendData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
