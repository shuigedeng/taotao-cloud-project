package com.taotao.cloud.mq.client.consumer.support.broker;

import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.mq.broker.dto.BrokerRegisterReq;
import com.taotao.cloud.mq.broker.dto.ServiceEntry;
import com.taotao.cloud.mq.broker.dto.consumer.ConsumerSubscribeReq;
import com.taotao.cloud.mq.broker.dto.consumer.ConsumerUnSubscribeReq;
import com.taotao.cloud.mq.broker.utils.InnerChannelUtils;
import com.taotao.cloud.mq.client.consumer.constant.ConsumerRespCode;
import com.taotao.cloud.mq.client.consumer.handler.MqConsumerHandler;
import com.taotao.cloud.mq.client.consumer.support.listener.IMqListenerService;
import com.taotao.cloud.mq.common.constant.MethodType;
import com.taotao.cloud.mq.common.dto.req.MqCommonReq;
import com.taotao.cloud.mq.common.dto.req.MqConsumerPullReq;
import com.taotao.cloud.mq.common.dto.req.MqConsumerUpdateStatusBatchReq;
import com.taotao.cloud.mq.common.dto.req.MqConsumerUpdateStatusReq;
import com.taotao.cloud.mq.common.dto.req.MqHeartBeatReq;
import com.taotao.cloud.mq.common.dto.req.component.MqConsumerUpdateStatusDto;
import com.taotao.cloud.mq.common.dto.resp.MqCommonResp;
import com.taotao.cloud.mq.common.dto.resp.MqConsumerPullResp;
import com.taotao.cloud.mq.common.resp.ConsumerStatus;
import com.taotao.cloud.mq.common.resp.MqCommonRespCode;
import com.taotao.cloud.mq.common.resp.MqException;
import com.taotao.cloud.mq.common.retry.core.core.Retryer;
import com.taotao.cloud.mq.common.rpc.RpcChannelFuture;
import com.taotao.cloud.mq.common.rpc.RpcMessageDto;
import com.taotao.cloud.mq.common.support.invoke.IInvokeService;
import com.taotao.cloud.mq.common.support.status.IStatusManager;
import com.taotao.cloud.mq.common.tmp.ILoadBalance;
import com.taotao.cloud.mq.common.util.ChannelFutureUtils;
import com.taotao.cloud.mq.common.util.ChannelUtil;
import com.taotao.cloud.mq.common.util.DelimiterUtil;
import com.taotao.cloud.mq.common.util.RandomUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.dromara.hutool.core.net.NetUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class ConsumerBrokerService implements IConsumerBrokerService {

	private static final Logger log = LoggerFactory.getLogger(ConsumerBrokerService.class);

	/**
	 * 分组名称
	 */
	private String groupName;

	/**
	 * 中间人地址
	 */
	private String brokerAddress;

	/**
	 * 调用管理服务
	 *
	 * @since 2024.05
	 */
	private IInvokeService invokeService;

	/**
	 * 获取响应超时时间
	 *
	 * @since 2024.05
	 */
	private long respTimeoutMills;

	/**
	 * 请求列表
	 *
	 * @since 2024.05
	 */
	private List<RpcChannelFuture> channelFutureList;

	/**
	 * 检测 broker 可用性
	 *
	 * @since 2024.05
	 */
	private boolean check;

	/**
	 * 状态管理
	 *
	 * @since 2024.05
	 */
	private IStatusManager statusManager;

	/**
	 * 监听服务类
	 */
	private IMqListenerService mqListenerService;

	/**
	 * 心跳定时任务
	 *
	 * @since 2024.05
	 */
	private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	/**
	 * 负载均衡策略
	 *
	 * @since 2024.05
	 */
	private ILoadBalance<RpcChannelFuture> loadBalance;

	/**
	 * 订阅最大尝试次数
	 *
	 * @since 2024.05
	 */
	private int subscribeMaxAttempt;

	/**
	 * 取消订阅最大尝试次数
	 */
	private int unSubscribeMaxAttempt;

	/**
	 * 消费状态更新最大尝试次数
	 */
	private int consumerStatusMaxAttempt;

	/**
	 * 账户标识
	 *
	 * @since 2024.05
	 */
	protected String appKey;

	/**
	 * 账户密码
	 *
	 * @since 2024.05
	 */
	protected String appSecret;

	@Override
	public void initChannelFutureList(ConsumerBrokerConfig config) {
		//1. 配置初始化
		this.invokeService = config.invokeService();
		this.check = config.check();
		this.respTimeoutMills = config.respTimeoutMills();
		this.brokerAddress = config.brokerAddress();
		this.groupName = config.groupName();
		this.statusManager = config.statusManager();
		this.mqListenerService = config.mqListenerService();
		this.loadBalance = config.loadBalance();
		this.subscribeMaxAttempt = config.subscribeMaxAttempt();
		this.unSubscribeMaxAttempt = config.unSubscribeMaxAttempt();
		this.consumerStatusMaxAttempt = config.consumerStatusMaxAttempt();
		this.appKey = config.appKey();
		this.appSecret = config.appSecret();

		//2. 初始化
		this.channelFutureList = ChannelFutureUtils.initChannelFutureList(brokerAddress,
			initChannelHandler(), check);

		//3. 初始化心跳
		this.initHeartbeat();
	}

	/**
	 * 初始化心跳
	 *
	 * @since 2024.05
	 */
	private void initHeartbeat() {
		//5S 发一次心跳
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				heartbeat();
			}
		}, 5, 5, TimeUnit.SECONDS);
	}

	private ChannelHandler initChannelHandler() {
		final ByteBuf delimiterBuf = DelimiterUtil.getByteBuf(DelimiterUtil.DELIMITER);

		final MqConsumerHandler mqProducerHandler = new MqConsumerHandler();
		mqProducerHandler.setInvokeService(invokeService);
		mqProducerHandler.setMqListenerService(mqListenerService);

		// handler 实际上会被多次调用，如果不是 @Shareable，应该每次都重新创建。
		ChannelHandler handler = new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel ch) throws Exception {
				ch.pipeline()
					.addLast(new DelimiterBasedFrameDecoder(DelimiterUtil.LENGTH, delimiterBuf))
					.addLast(mqProducerHandler);
			}
		};

		return handler;
	}

	@Override
	public void registerToBroker() {
		int successCount = 0;

		for (RpcChannelFuture channelFuture : this.channelFutureList) {
			ServiceEntry serviceEntry = new ServiceEntry();
			serviceEntry.setGroupName(groupName);
			serviceEntry.setAddress(channelFuture.getAddress());
			serviceEntry.setPort(channelFuture.getPort());
			serviceEntry.setWeight(channelFuture.getWeight());

			BrokerRegisterReq brokerRegisterReq = new BrokerRegisterReq();
			brokerRegisterReq.setServiceEntry(serviceEntry);
			brokerRegisterReq.setMethodType(MethodType.C_REGISTER);
			brokerRegisterReq.setTraceId(RandomUtil.randomString(32));
			brokerRegisterReq.setAppKey(appKey);
			brokerRegisterReq.setAppSecret(appSecret);

			log.info("[Register] 开始注册到 broker：{}", JSON.toJSON(brokerRegisterReq));
			final Channel channel = channelFuture.getChannelFuture().channel();
			MqCommonResp resp = callServer(channel, brokerRegisterReq, MqCommonResp.class);
			log.info("[Register] 完成注册到 broker：{}", JSON.toJSON(resp));

			if (MqCommonRespCode.SUCCESS.getCode().equals(resp.getRespCode())) {
				successCount++;
			}
		}

		if (successCount <= 0 && check) {
			log.error("校验 broker 可用性，可连接成功数为 0");
			throw new MqException(MqCommonRespCode.C_REGISTER_TO_BROKER_FAILED);
		}
	}

	@Override
	public <T extends MqCommonReq, R extends MqCommonResp> R callServer(Channel channel,
		T commonReq, Class<R> respClass) {
		final String traceId = commonReq.getTraceId();
		final long requestTime = System.currentTimeMillis();

		RpcMessageDto rpcMessageDto = new RpcMessageDto();
		rpcMessageDto.setTraceId(traceId);
		rpcMessageDto.setRequestTime(requestTime);
		rpcMessageDto.setJson(JSON.toJSONString(commonReq));
		rpcMessageDto.setMethodType(commonReq.getMethodType());
		rpcMessageDto.setRequest(true);

		// 添加调用服务
		invokeService.addRequest(traceId, respTimeoutMills);

		// 遍历 channel
		// 关闭当前线程，以获取对应的信息
		// 使用序列化的方式
		ByteBuf byteBuf = DelimiterUtil.getMessageDelimiterBuffer(rpcMessageDto);

		//负载均衡获取 channel
		channel.writeAndFlush(byteBuf);

		String channelId = ChannelUtil.getChannelId(channel);
		log.debug("[Client] channelId {} 发送消息 {}", channelId, JSON.toJSON(rpcMessageDto));
        channel.closeFuture().syncUninterruptibly();

		if (respClass == null) {
			log.debug("[Client] 当前消息为 one-way 消息，忽略响应");
			return null;
		}
		else {
			//channelHandler 中获取对应的响应
			RpcMessageDto messageDto = invokeService.getResponse(traceId);
			if (MqCommonRespCode.TIMEOUT.getCode().equals(messageDto.getRespCode())) {
				throw new MqException(MqCommonRespCode.TIMEOUT);
			}

			String respJson = messageDto.getJson();
			return JSON.parseObject(respJson, respClass);
		}
	}

	@Override
	public Channel getChannel(String key) {
		// 等待启动完成
		while (!statusManager.status()) {
			if (statusManager.initFailed()) {
				log.error("初始化失败");
				throw new MqException(MqCommonRespCode.C_INIT_FAILED);
			}

			log.debug("等待初始化完成...");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		RpcChannelFuture rpcChannelFuture = RandomUtils.loadBalance(loadBalance,
			channelFutureList, key);
		return rpcChannelFuture.getChannelFuture().channel();
	}

	@Override
	public void subscribe(String topicName, String tagRegex, String consumerType) {
		final ConsumerSubscribeReq req = new ConsumerSubscribeReq();

		String messageId = RandomUtil.randomString(32);
		req.setTraceId(messageId);
		req.setMethodType(MethodType.C_SUBSCRIBE);
		req.setTopicName(topicName);
		req.setTagRegex(tagRegex);
		req.setGroupName(groupName);
		req.setConsumerType(consumerType);

		// 重试订阅
		Retryer.<String>newInstance()
			.maxAttempt(subscribeMaxAttempt)
			.callable(new Callable<String>() {
				@Override
				public String call() throws Exception {
					Channel channel = getChannel(null);
					MqCommonResp resp = callServer(channel, req, MqCommonResp.class);
					if (!MqCommonRespCode.SUCCESS.getCode().equals(resp.getRespCode())) {
						throw new MqException(ConsumerRespCode.SUBSCRIBE_FAILED);
					}
					return resp.getRespCode();
				}
			}).retryCall();
	}

	@Override
	public void unSubscribe(String topicName, String tagRegex, String consumerType) {
		final ConsumerUnSubscribeReq req = new ConsumerUnSubscribeReq();

		String messageId =  RandomUtil.randomString(32);
		req.setTraceId(messageId);
		req.setMethodType(MethodType.C_UN_SUBSCRIBE);
		req.setTopicName(topicName);
		req.setTagRegex(tagRegex);
		req.setGroupName(groupName);
		req.setConsumerType(consumerType);

		// 重试取消订阅
		Retryer.<String>newInstance()
			.maxAttempt(unSubscribeMaxAttempt)
			.callable(new Callable<String>() {
				@Override
				public String call() throws Exception {
					Channel channel = getChannel(null);
					MqCommonResp resp = callServer(channel, req, MqCommonResp.class);
					if (!MqCommonRespCode.SUCCESS.getCode().equals(resp.getRespCode())) {
						throw new MqException(ConsumerRespCode.UN_SUBSCRIBE_FAILED);
					}
					return resp.getRespCode();
				}
			}).retryCall();
	}

	@Override
	public void heartbeat() {
		final MqHeartBeatReq req = new MqHeartBeatReq();
		final String traceId = RandomUtil.randomString(32);
		req.setTraceId(traceId);
		req.setMethodType(MethodType.C_HEARTBEAT);
		req.setAddress(NetUtil.getLocalhostStrV4());
		req.setPort(0);
		req.setTime(System.currentTimeMillis());

		log.debug("[HEARTBEAT] 往服务端发送心跳包 {}", JSON.toJSON(req));

		// 通知全部
		for (RpcChannelFuture channelFuture : channelFutureList) {
			try {
				Channel channel = channelFuture.getChannelFuture().channel();
				callServer(channel, req, null);
			}
			catch (Exception exception) {
				log.error("[HEARTBEAT] 往服务端处理异常", exception);
			}
		}
	}

	@Override
	public MqConsumerPullResp pull(String topicName, String tagRegex, int fetchSize) {
		MqConsumerPullReq req = new MqConsumerPullReq();
		req.setSize(fetchSize);
		req.setGroupName(groupName);
		req.setTagRegex(tagRegex);
		req.setTopicName(topicName);

		final String traceId = RandomUtil.randomString(32);
		req.setTraceId(traceId);
		req.setMethodType(MethodType.C_MESSAGE_PULL);

		Channel channel = getChannel(null);
		return this.callServer(channel, req, MqConsumerPullResp.class);
	}

	@Override
	public MqCommonResp consumerStatusAck(String messageId, ConsumerStatus consumerStatus) {
		final MqConsumerUpdateStatusReq req = new MqConsumerUpdateStatusReq();
		req.setMessageId(messageId);
		req.setMessageStatus(consumerStatus.getCode());
		req.setConsumerGroupName(groupName);

		final String traceId = RandomUtil.randomString(32);
		req.setTraceId(traceId);
		req.setMethodType(MethodType.C_CONSUMER_STATUS);

		// 重试
		return Retryer.<MqCommonResp>newInstance()
			.maxAttempt(consumerStatusMaxAttempt)
			.callable(new Callable<MqCommonResp>() {
				@Override
				public MqCommonResp call() throws Exception {
					Channel channel = getChannel(null);
					MqCommonResp resp = callServer(channel, req, MqCommonResp.class);
					if (!MqCommonRespCode.SUCCESS.getCode().equals(resp.getRespCode())) {
						throw new MqException(ConsumerRespCode.CONSUMER_STATUS_ACK_FAILED);
					}
					return resp;
				}
			}).retryCall();
	}

	@Override
	public MqCommonResp consumerStatusAckBatch(List<MqConsumerUpdateStatusDto> statusDtoList) {
		final MqConsumerUpdateStatusBatchReq req = new MqConsumerUpdateStatusBatchReq();
		req.setStatusList(statusDtoList);

		final String traceId = RandomUtil.randomString(32);
		req.setTraceId(traceId);
		req.setMethodType(MethodType.C_CONSUMER_STATUS_BATCH);

		// 重试
		return Retryer.<MqCommonResp>newInstance()
			.maxAttempt(consumerStatusMaxAttempt)
			.callable(new Callable<MqCommonResp>() {
				@Override
				public MqCommonResp call() throws Exception {
					Channel channel = getChannel(null);
					MqCommonResp resp = callServer(channel, req, MqCommonResp.class);
					if (!MqCommonRespCode.SUCCESS.getCode().equals(resp.getRespCode())) {
						throw new MqException(ConsumerRespCode.CONSUMER_STATUS_ACK_BATCH_FAILED);
					}
					return resp;
				}
			}).retryCall();
	}

	@Override
	public void destroyAll() {
		for (RpcChannelFuture channelFuture : channelFutureList) {
			Channel channel = channelFuture.getChannelFuture().channel();
			final String channelId = ChannelUtil.getChannelId(channel);
			log.info("开始注销：{}", channelId);

			ServiceEntry serviceEntry = InnerChannelUtils.buildServiceEntry(channelFuture);

			BrokerRegisterReq brokerRegisterReq = new BrokerRegisterReq();
			brokerRegisterReq.setServiceEntry(serviceEntry);

			String messageId = RandomUtil.randomString(32);
			brokerRegisterReq.setTraceId(messageId);
			brokerRegisterReq.setMethodType(MethodType.C_UN_REGISTER);

			this.callServer(channel, brokerRegisterReq, null);

			log.info("完成注销：{}", channelId);
		}
	}

}
