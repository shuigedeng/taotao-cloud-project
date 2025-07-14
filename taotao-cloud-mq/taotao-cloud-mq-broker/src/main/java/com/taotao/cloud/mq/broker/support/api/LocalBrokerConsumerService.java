package com.taotao.cloud.mq.broker.support.api;

import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.mq.broker.api.IBrokerConsumerService;
import com.taotao.cloud.mq.broker.dto.BrokerServiceEntryChannel;
import com.taotao.cloud.mq.broker.dto.ChannelGroupNameDto;
import com.taotao.cloud.mq.broker.dto.ServiceEntry;
import com.taotao.cloud.mq.broker.dto.consumer.ConsumerSubscribeBo;
import com.taotao.cloud.mq.broker.dto.consumer.ConsumerSubscribeReq;
import com.taotao.cloud.mq.broker.dto.consumer.ConsumerUnSubscribeReq;
import com.taotao.cloud.mq.broker.resp.MqBrokerRespCode;
import com.taotao.cloud.mq.broker.utils.InnerChannelUtils;
import com.taotao.cloud.mq.common.dto.req.MqHeartBeatReq;
import com.taotao.cloud.mq.common.dto.req.MqMessage;
import com.taotao.cloud.mq.common.dto.resp.MqCommonResp;
import com.taotao.cloud.mq.common.resp.MqCommonRespCode;
import com.taotao.cloud.mq.common.resp.MqException;
import com.taotao.cloud.mq.common.tmp.ILoadBalance;
import com.taotao.cloud.mq.common.util.ChannelUtil;
import com.taotao.cloud.mq.common.util.MapUtil;
import com.taotao.cloud.mq.common.util.RandomUtils;
import com.taotao.cloud.mq.common.util.RegexUtil;
import com.xkzhangsan.time.utils.CollectionUtil;
import io.netty.channel.Channel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class LocalBrokerConsumerService implements IBrokerConsumerService {

	private static final Logger LOG = LoggerFactory.getLogger(LocalBrokerConsumerService.class);

	private final Map<String, BrokerServiceEntryChannel> registerMap = new ConcurrentHashMap<>();

	/**
	 * 订阅集合-推送策略 key: topicName value: 对应的订阅列表
	 */
	private final Map<String, Set<ConsumerSubscribeBo>> pushSubscribeMap = new ConcurrentHashMap<>();

	/**
	 * 心跳 map
	 *
	 * @since 2024.05
	 */
	private final Map<String, BrokerServiceEntryChannel> heartbeatMap = new ConcurrentHashMap<>();

	/**
	 * 心跳定时任务
	 *
	 * @since 2024.05
	 */
	private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	/**
	 * 负载均衡策略
	 *
	 * @since 2024.05
	 */
	private ILoadBalance<ConsumerSubscribeBo> loadBalance;

	public LocalBrokerConsumerService() {
		//120S 扫描一次
		final long limitMills = 2 * 60 * 1000;
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				for (Map.Entry<String, BrokerServiceEntryChannel> entry : heartbeatMap.entrySet()) {
					String key = entry.getKey();
					long lastAccessTime = entry.getValue().getLastAccessTime();
					long currentTime = System.currentTimeMillis();

					if (currentTime - lastAccessTime > limitMills) {
						removeByChannelId(key);
					}
				}
			}
		}, 2 * 60, 2 * 60, TimeUnit.SECONDS);
	}

	@Override
	public void loadBalance(ILoadBalance<ConsumerSubscribeBo> loadBalance) {
		this.loadBalance = loadBalance;
	}

	@Override
	public MqCommonResp register(ServiceEntry serviceEntry, Channel channel) {
		final String channelId = ChannelUtil.getChannelId(channel);
		BrokerServiceEntryChannel entryChannel = InnerChannelUtils.buildEntryChannel(serviceEntry,
			channel);
		registerMap.put(channelId, entryChannel);

		entryChannel.setLastAccessTime(System.currentTimeMillis());
		heartbeatMap.put(channelId, entryChannel);

		MqCommonResp resp = new MqCommonResp();
		resp.setRespCode(MqCommonRespCode.SUCCESS.getCode());
		resp.setRespMessage(MqCommonRespCode.SUCCESS.getMsg());
		return resp;
	}

	@Override
	public MqCommonResp unRegister(ServiceEntry serviceEntry, Channel channel) {
		final String channelId = ChannelUtil.getChannelId(channel);
		removeByChannelId(channelId);

		MqCommonResp resp = new MqCommonResp();
		resp.setRespCode(MqCommonRespCode.SUCCESS.getCode());
		resp.setRespMessage(MqCommonRespCode.SUCCESS.getMsg());
		return resp;
	}

	/**
	 * 根据 channelId 移除信息
	 *
	 * @param channelId 通道唯一标识
	 * @since 2024.05
	 */
	private void removeByChannelId(final String channelId) {
		BrokerServiceEntryChannel channelRegister = registerMap.remove(channelId);
		LOG.info("移除注册信息 id: {}, channel: {}", channelId, JSON.toJSON(channelRegister));
		BrokerServiceEntryChannel channelHeartbeat = heartbeatMap.remove(channelId);
		LOG.info("移除心跳信息 id: {}, channel: {}", channelId, JSON.toJSON(channelHeartbeat));
	}

	@Override
	public MqCommonResp subscribe(ConsumerSubscribeReq serviceEntry, Channel clientChannel) {
		final String channelId = ChannelUtil.getChannelId(clientChannel);
		final String topicName = serviceEntry.getTopicName();

		final String consumerType = serviceEntry.getConsumerType();
		Map<String, Set<ConsumerSubscribeBo>> subscribeMap = getSubscribeMapByConsumerType(
			consumerType);

		ConsumerSubscribeBo subscribeBo = new ConsumerSubscribeBo();
		subscribeBo.setChannelId(channelId);
		subscribeBo.setGroupName(serviceEntry.getGroupName());
		subscribeBo.setTopicName(topicName);
		subscribeBo.setTagRegex(serviceEntry.getTagRegex());

		// 放入集合
		MapUtil.putToSetMap(subscribeMap, topicName, subscribeBo);

		MqCommonResp resp = new MqCommonResp();
		resp.setRespCode(MqCommonRespCode.SUCCESS.getCode());
		resp.setRespMessage(MqCommonRespCode.SUCCESS.getMsg());
		return resp;
	}

	private Map<String, Set<ConsumerSubscribeBo>> getSubscribeMapByConsumerType(
		String consumerType) {
		return pushSubscribeMap;
	}

	@Override
	public MqCommonResp unSubscribe(ConsumerUnSubscribeReq serviceEntry, Channel clientChannel) {
		final String channelId = ChannelUtil.getChannelId(clientChannel);
		final String topicName = serviceEntry.getTopicName();
		final String consumerType = serviceEntry.getConsumerType();
		Map<String, Set<ConsumerSubscribeBo>> subscribeMap = getSubscribeMapByConsumerType(
			consumerType);

		ConsumerSubscribeBo subscribeBo = new ConsumerSubscribeBo();
		subscribeBo.setChannelId(channelId);
		subscribeBo.setGroupName(serviceEntry.getGroupName());
		subscribeBo.setTopicName(topicName);
		subscribeBo.setTagRegex(serviceEntry.getTagRegex());

		// 集合
		Set<ConsumerSubscribeBo> set = subscribeMap.get(topicName);
		if (CollectionUtil.isNotEmpty(set)) {
			set.remove(subscribeBo);
		}

		MqCommonResp resp = new MqCommonResp();
		resp.setRespCode(MqCommonRespCode.SUCCESS.getCode());
		resp.setRespMessage(MqCommonRespCode.SUCCESS.getMsg());
		return resp;
	}

	@Override
	public List<ChannelGroupNameDto> getPushSubscribeList(MqMessage mqMessage) {
		final String topicName = mqMessage.getTopic();
		Set<ConsumerSubscribeBo> set = pushSubscribeMap.get(topicName);
		if (CollectionUtil.isEmpty(set)) {
			return Collections.emptyList();
		}

		//2. 获取匹配的 tag 列表
		final List<String> tagNameList = mqMessage.getTags();

		Map<String, List<ConsumerSubscribeBo>> groupMap = new HashMap<>();
		for (ConsumerSubscribeBo bo : set) {
			String tagRegex = bo.getTagRegex();

			if (RegexUtil.hasMatch(tagNameList, tagRegex)) {
				String groupName = bo.getGroupName();

				MapUtil.putToListMap(groupMap, groupName, bo);
			}
		}

		//3. 按照 groupName 分组之后，每一组只随机返回一个。最好应该调整为以 shardingkey 选择
		final String shardingKey = mqMessage.getShardingKey();
		List<ChannelGroupNameDto> channelGroupNameList = new ArrayList<>();

		for (Map.Entry<String, List<ConsumerSubscribeBo>> entry : groupMap.entrySet()) {
			List<ConsumerSubscribeBo> list = entry.getValue();

			ConsumerSubscribeBo bo = RandomUtils.loadBalance(loadBalance, list, shardingKey);
			final String channelId = bo.getChannelId();
			BrokerServiceEntryChannel entryChannel = registerMap.get(channelId);
			if (entryChannel == null) {
				LOG.warn("channelId: {} 对应的通道信息为空", channelId);
				continue;
			}

			final String groupName = entry.getKey();
			ChannelGroupNameDto channelGroupNameDto = ChannelGroupNameDto.of(groupName,
				entryChannel.getChannel());
			channelGroupNameList.add(channelGroupNameDto);
		}

		return channelGroupNameList;
	}

	@Override
	public void heartbeat(MqHeartBeatReq mqHeartBeatReq, Channel channel) {
		final String channelId = ChannelUtil.getChannelId(channel);
		LOG.info("[HEARTBEAT] 接收消费者心跳 {}, channelId: {}",
			JSON.toJSON(mqHeartBeatReq), channelId);

		ServiceEntry serviceEntry = new ServiceEntry();
		serviceEntry.setAddress(mqHeartBeatReq.getAddress());
		serviceEntry.setPort(mqHeartBeatReq.getPort());

		BrokerServiceEntryChannel entryChannel = InnerChannelUtils.buildEntryChannel(serviceEntry,
			channel);
		entryChannel.setLastAccessTime(mqHeartBeatReq.getTime());

		heartbeatMap.put(channelId, entryChannel);
	}

	@Override
	public void checkValid(String channelId) {
		if (!registerMap.containsKey(channelId)) {
			LOG.error("channelId: {} 未注册", channelId);
			throw new MqException(MqBrokerRespCode.C_REGISTER_CHANNEL_NOT_VALID);
		}
	}

}
