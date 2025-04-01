package com.taotao.cloud.rpc.client;


import com.taotao.cloud.rpc.client.handler.RpcClientHandler;
import com.taotao.cloud.rpc.common.common.support.invoke.impl.DefaultInvokeManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> rpc 客户端 </p>
 */
public class RpcClient extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(RpcClient.class);


	/**
	 * 监听端口号
	 */
	private final int port;

	public RpcClient(int port) {
		this.port = port;
	}

	public RpcClient() {
		this(9527);
	}

	@Override
	public void run() {
		// 启动服务端
		LOG.info("RPC 服务开始启动客户端");

		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap bootstrap = new Bootstrap();
			ChannelFuture channelFuture = bootstrap.group(workerGroup)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline()
							.addLast(new LoggingHandler(LogLevel.INFO))
							.addLast(new RpcClientHandler(new DefaultInvokeManager()));
					}
				})
				.connect("localhost", port)
				.syncUninterruptibly();

			LOG.info("RPC 服务启动客户端完成，监听端口：" + port);
			channelFuture.channel().closeFuture().syncUninterruptibly();
			LOG.info("RPC 服务开始客户端已关闭");
		} catch (Exception e) {
			LOG.error("RPC 客户端遇到异常", e);
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
}
