package com.taotao.cloud.mq.dashboard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MqBootstrap {

	public static void main(String[] args) {
		// 配置服务端NIO线程组
		EventLoopGroup parentGroup =
			new NioEventLoopGroup(); // NioEventLoopGroup extends MultithreadEventLoopGroup Math.max(1,
		// SystemPropertyUtil.getInt("io.netty.eventLoopThreads",
		// NettyRuntime.availableProcessors() * 2));
		EventLoopGroup childGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(parentGroup, childGroup)
				.channel(NioServerSocketChannel.class) // 非阻塞模式
				.option(ChannelOption.SO_BACKLOG, 128)
				.childHandler(new MyChannelInitializer());
			ChannelFuture f = b.bind(8080).sync();
			System.out.println("com.lm.netty02 server start done. {关注明哥，获取源码}");
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			childGroup.shutdownGracefully();
			parentGroup.shutdownGracefully();
		}
	}
}
