package com.taotao.cloud.tx.rm.netty;

import com.alibaba.fastjson2.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

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
	public void start(String host, int port) {
		client = new NettyClientHandler();
		Bootstrap bootstrap = new Bootstrap();
		EventLoopGroup group = new NioEventLoopGroup();
		bootstrap.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ClientInitializer(client));
		try {
			bootstrap.connect(host, port).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void send(JSONObject sendData) {
		try {
			client.sendData(sendData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
