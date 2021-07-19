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

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * NettyServerHandler
 *
 * @author shuigedeng
 * @since 2020/12/30 下午4:36
 * @version 1.0.0
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	private static final Lock lock_1 = new ReentrantLock();

	private static final Lock lock_2 = new ReentrantLock();

	private static final Lock lock_3 = new ReentrantLock();

	private static final Lock lock_4 = new ReentrantLock();

	/**
	 * 管理一个全局map，保存连接进服务端的通道数量
	 */
	private static final ConcurrentHashMap<ChannelId, ChannelHandlerContext> CHANNEL_MAP = new ConcurrentHashMap<>();

	@Override //数据读取完毕
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//writeAndFlush 是 write + flush
		//将数据写入到缓存，并刷新
		//一般讲，我们对这个发送的数据进行编码
		//ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵1", CharsetUtil.UTF_8));
	}

	/**
	 * 处理异常, 一般是需要关闭通道
	 *
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		log.info("服务端异常关闭" + ctx.channel());
	}

	/**
	 * @param ctx
	 * @DESCRIPTION: 有客户端连接服务器会触发此函数
	 * @return: void
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		lock_1.lock();
		try {
			//获取连接通道唯一标识
			ChannelId channelId = ctx.channel().id();
			//如果map中不包含此连接，就保存连接
			if (CHANNEL_MAP.containsKey(channelId)) {
				log.info("客户端【" + channelId + "】是连接状态，连接通道数量: " + CHANNEL_MAP.size());
			} else {
				//保存连接
				CHANNEL_MAP.put(channelId, ctx);

				log.info("客户端【" + channelId + "】连接netty服务器");
				log.info("连接通道数量: " + CHANNEL_MAP.size());
			}
		} finally {
			lock_1.unlock();
		}
	}

	/**
	 * @param ctx
	 * @DESCRIPTION: 有客户端终止连接服务器会触发此函数
	 * @return: void
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		lock_2.lock();
		try {
			ChannelId channelId = ctx.channel().id();
			//包含此客户端才去删除
			if (CHANNEL_MAP.containsKey(channelId)) {
				//删除连接
				CHANNEL_MAP.remove(channelId);
				System.out.println();
				log.info("客户端【" + channelId + "】退出netty服务器");
				log.info("连接通道数量: " + CHANNEL_MAP.size());
			}
		} finally {
			lock_2.unlock();
		}
	}


	/**
	 * 1. ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel, 地址 2. Object msg: 就是客户端发送的数据
	 * 默认Object
	 * <p>
	 * 读取数据实际(这里我们可以读取客户端发送的消息)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		lock_3.lock();
		try {
			log.info("服务器读取线程 " + Thread.currentThread().getName() + " channle = " + ctx.channel());
			Channel channel = ctx.channel();
			//将 msg 转成一个 ByteBuf
			//ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer.
			ByteBuf buf = (ByteBuf) msg;
			//得到此时客户端的数据长度
			int bytesLength = buf.readableBytes();
			//组件新的字节数组
			byte[] buffer = new byte[bytesLength];
			buf.readBytes(buffer);
			final String allData = NettyByteAndStringUtils.byteToHex(buffer);

			log.info("进入服务端数据：" + allData);
			ctx.executor().execute(new NettySendThread(ctx, allData));

		} finally {
			lock_3.unlock();
		}
	}


	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		lock_4.lock();
		try {
			String socketString = ctx.channel().remoteAddress().toString();
			if (evt instanceof IdleStateEvent) {
				IdleStateEvent event = (IdleStateEvent) evt;
				if (event.state() == IdleState.READER_IDLE) {
					log.info("Client: " + socketString + " READER_IDLE 读超时");
					ctx.disconnect();
				} else if (event.state() == IdleState.WRITER_IDLE) {
					log.info("Client: " + socketString + " WRITER_IDLE 写超时");
					ctx.disconnect();
				} else if (event.state() == IdleState.ALL_IDLE) {
					log.info("Client: " + socketString + " ALL_IDLE 总超时");
					ctx.disconnect();
				}
			}
		} finally {
			lock_4.unlock();
		}
	}
}
