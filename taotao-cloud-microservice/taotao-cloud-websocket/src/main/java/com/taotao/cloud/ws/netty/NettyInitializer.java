/*
 * Copyright 2002-2021 the original author or authors.
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

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * NettyInitializer
 *
 * @author shuigedeng
 * @since 2020/12/30 下午4:45
 * @version 1.0.0
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyInitializer extends ChannelInitializer {

	@Override
	protected void initChannel(Channel channel) throws Exception {
		//创建一个通道初始化对象(匿名对象)
		//给pipeline 设置处理器
		//可以使用一个集合管理 SocketChannel,再推送消息时,可以将业务加入到各个channel对应的NIOEventLoop的taskQueue
		// 或者 scheduleTaskQueue
		log.info("客户socketchannel hashcode=" + channel.hashCode());
		//入站解码
		//channel.pipeline().addLast(new MyByteToLongDecoder2());
		//出站编码
		channel.pipeline().addLast(new NettyByteEncoder());
		channel.pipeline().addLast(new NettyServerHandler());
	}
}
