package com.taotao.cloud.mq.broker.core;

import com.taotao.cloud.mq.broker.api.IBrokerConsumerService;
import com.taotao.cloud.mq.broker.api.IBrokerProducerService;
import com.taotao.cloud.mq.broker.api.IMqBroker;
import com.taotao.cloud.mq.broker.constant.BrokerConst;
import com.taotao.cloud.mq.broker.constant.BrokerRespCode;
import com.taotao.cloud.mq.broker.dto.consumer.ConsumerSubscribeBo;
import com.taotao.cloud.mq.broker.handler.MqBrokerHandler;
import com.taotao.cloud.mq.broker.support.api.LocalBrokerConsumerService;
import com.taotao.cloud.mq.broker.support.api.LocalBrokerProducerService;
import com.taotao.cloud.mq.broker.support.persist.IMqBrokerPersist;
import com.taotao.cloud.mq.broker.support.persist.LocalMqBrokerPersist;
import com.taotao.cloud.mq.broker.support.push.BrokerPushService;
import com.taotao.cloud.mq.broker.support.push.IBrokerPushService;
import com.taotao.cloud.mq.broker.support.valid.BrokerRegisterValidService;
import com.taotao.cloud.mq.broker.support.valid.IBrokerRegisterValidService;
import com.taotao.cloud.mq.common.balance.ILoadBalance;
import com.taotao.cloud.mq.common.balance.impl.LoadBalances;
import com.taotao.cloud.mq.common.resp.MqException;
import com.taotao.cloud.mq.common.support.invoke.IInvokeService;
import com.taotao.cloud.mq.common.support.invoke.impl.InvokeService;
import com.taotao.cloud.mq.common.util.DelimiterUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqBroker extends Thread implements IMqBroker {

	private static final Logger log = LoggerFactory.getLogger(MqBroker.class);

	/**
	 * 端口号
	 */
	private int port = BrokerConst.DEFAULT_PORT;

	/**
	 * 调用管理类
	 *
	 * @since 2024.05
	 */
	private final IInvokeService invokeService = new InvokeService();

	/**
	 * 消费者管理
	 *
	 * @since 2024.05
	 */
	private IBrokerConsumerService registerConsumerService = new LocalBrokerConsumerService();

	/**
	 * 生产者管理
	 *
	 * @since 2024.05
	 */
	private IBrokerProducerService registerProducerService = new LocalBrokerProducerService();

	/**
	 * 持久化类
	 *
	 * @since 2024.05
	 */
	private IMqBrokerPersist mqBrokerPersist = new LocalMqBrokerPersist();

	/**
	 * 推送服务
	 *
	 * @since 2024.05
	 */
	private IBrokerPushService brokerPushService = new BrokerPushService();

	/**
	 * 获取响应超时时间
	 *
	 * @since 2024.05
	 */
	private long respTimeoutMills = 5000;

	/**
	 * 负载均衡
	 *
	 * @since 2024.05
	 */
	private ILoadBalance<ConsumerSubscribeBo> loadBalance = LoadBalances.weightRoundRobbin();

	/**
	 * 推送最大尝试次数
	 *
	 * @since 2024.05
	 */
	private int pushMaxAttempt = 3;

	/**
	 * 注册验证服务类
	 *
	 * @since 2024.05
	 */
	private IBrokerRegisterValidService brokerRegisterValidService = new BrokerRegisterValidService();

	public MqBroker port(int port) {
		this.port = port;
		return this;
	}

	public MqBroker registerConsumerService(IBrokerConsumerService registerConsumerService) {
		this.registerConsumerService = registerConsumerService;
		return this;
	}

	public MqBroker registerProducerService(IBrokerProducerService registerProducerService) {
		this.registerProducerService = registerProducerService;
		return this;
	}

	public MqBroker mqBrokerPersist(IMqBrokerPersist mqBrokerPersist) {
		this.mqBrokerPersist = mqBrokerPersist;
		return this;
	}

	public MqBroker brokerPushService(IBrokerPushService brokerPushService) {
		this.brokerPushService = brokerPushService;
		return this;
	}

	public MqBroker respTimeoutMills(long respTimeoutMills) {
		this.respTimeoutMills = respTimeoutMills;
		return this;
	}

	public MqBroker loadBalance(ILoadBalance<ConsumerSubscribeBo> loadBalance) {
		this.loadBalance = loadBalance;
		return this;
	}

	public MqBroker pushMaxAttempt(int pushMaxAttempt) {
		this.pushMaxAttempt = pushMaxAttempt;
		return this;
	}

	public MqBroker brokerRegisterValidService(
		IBrokerRegisterValidService brokerRegisterValidService) {
		this.brokerRegisterValidService = brokerRegisterValidService;
		return this;
	}

	private ChannelHandler initChannelHandler() {
		registerConsumerService.loadBalance(this.loadBalance);

		MqBrokerHandler handler = new MqBrokerHandler();
		handler.invokeService(invokeService)
			.respTimeoutMills(respTimeoutMills)
			.registerConsumerService(registerConsumerService)
			.registerProducerService(registerProducerService)
			.mqBrokerPersist(mqBrokerPersist)
			.brokerPushService(brokerPushService)
			.respTimeoutMills(respTimeoutMills)
			.pushMaxAttempt(pushMaxAttempt)
			.brokerRegisterValidService(brokerRegisterValidService);

		return handler;
	}

	@Override
	public void run() {
		// 启动服务端
		log.info("MQ 中间人开始启动服务端 port: {}", port);

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			final ByteBuf delimiterBuf = DelimiterUtil.getByteBuf(DelimiterUtil.DELIMITER);
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(workerGroup, bossGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline()
							.addLast(
								new DelimiterBasedFrameDecoder(DelimiterUtil.LENGTH, delimiterBuf))
							.addLast(initChannelHandler());
					}
				})
				// 这个参数影响的是还没有被accept 取出的连接
				.option(ChannelOption.SO_BACKLOG, 128)
				// 这个参数只是过一段时间内客户端没有响应，服务端会发送一个 ack 包，以判断客户端是否还活着。
				.childOption(ChannelOption.SO_KEEPALIVE, true);

			// 绑定端口，开始接收进来的链接
			ChannelFuture channelFuture = serverBootstrap.bind(port).syncUninterruptibly();
			log.info("MQ 中间人启动完成，监听【" + port + "】端口");

			channelFuture.channel().closeFuture().syncUninterruptibly();
			log.info("MQ 中间人关闭完成");
		}
		catch (Exception e) {
			log.error("MQ 中间人启动异常", e);
			throw new MqException(BrokerRespCode.RPC_INIT_FAILED);
		}
		finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

}
