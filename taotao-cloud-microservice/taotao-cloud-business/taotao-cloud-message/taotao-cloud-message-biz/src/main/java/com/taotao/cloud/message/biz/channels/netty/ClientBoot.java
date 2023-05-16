package com.taotao.cloud.message.biz.channels.netty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientBoot {
	@Autowired
	Bootstrap bootstrap;
	@Autowired
    HoleNettyProperties holeNettyProperties;

	/**
	 * 主端口连接
	 *
	 * @return
	 * @throws InterruptedException
	 */
	public Channel connect() throws InterruptedException {
		// 连接服务器
		ChannelFuture channelFuture = bootstrap.connect(holeNettyProperties.getHost(), holeNettyProperties.getPort()).sync();
		// 监听关闭
		Channel channel = channelFuture.channel();
		return channel;
	}

	/**
	 * 备用端口连接
	 *
	 * @return
	 * @throws InterruptedException
	 */
	public Channel connectSlave() throws InterruptedException {
		// 连接服务器
		ChannelFuture channelFuture = bootstrap.connect(holeNettyProperties.getHost(), holeNettyProperties.getPort()).sync();
		// 监听关闭
		Channel channel = channelFuture.channel();
		channel.closeFuture().sync();
		return channel;
	}

	/**
	 * 发送消息到服务器端
	 *
	 * @return
	 */
	public void sendMsg(MessageBean messageBean) throws InterruptedException {
		connect().writeAndFlush(messageBean);
	}
}

