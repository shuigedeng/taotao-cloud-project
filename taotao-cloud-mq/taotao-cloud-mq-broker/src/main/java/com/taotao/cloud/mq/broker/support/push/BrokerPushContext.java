package com.taotao.cloud.mq.broker.support.push;

import com.taotao.cloud.mq.broker.dto.ChannelGroupNameDto;
import com.taotao.cloud.mq.broker.dto.persist.MqMessagePersistPut;
import com.taotao.cloud.mq.broker.support.persist.IMqBrokerPersist;
import com.taotao.cloud.mq.common.support.invoke.IInvokeService;
import java.util.List;
import java.util.Map;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class BrokerPushContext {

	private IMqBrokerPersist mqBrokerPersist;

	private MqMessagePersistPut mqMessagePersistPut;

	private List<ChannelGroupNameDto> channelList;

	private IInvokeService invokeService;

	/**
	 * 获取响应超时时间
	 *
	 * @since 2024.05
	 */
	private long respTimeoutMills;

	/**
	 * 推送最大尝试次数
	 *
	 * @since 2024.05
	 */
	private int pushMaxAttempt;

	/**
	 * channel 标识和 groupName map
	 *
	 * @since 2024.05
	 */
	private Map<String, String> channelGroupMap;

	public static BrokerPushContext newInstance() {
		return new BrokerPushContext();
	}

	public IMqBrokerPersist mqBrokerPersist() {
		return mqBrokerPersist;
	}

	public BrokerPushContext mqBrokerPersist(IMqBrokerPersist mqBrokerPersist) {
		this.mqBrokerPersist = mqBrokerPersist;
		return this;
	}

	public MqMessagePersistPut mqMessagePersistPut() {
		return mqMessagePersistPut;
	}

	public BrokerPushContext mqMessagePersistPut(MqMessagePersistPut mqMessagePersistPut) {
		this.mqMessagePersistPut = mqMessagePersistPut;
		return this;
	}

	public List<ChannelGroupNameDto> channelList() {
		return channelList;
	}

	public BrokerPushContext channelList(List<ChannelGroupNameDto> channelList) {
		this.channelList = channelList;
		return this;
	}

	public IInvokeService invokeService() {
		return invokeService;
	}

	public BrokerPushContext invokeService(IInvokeService invokeService) {
		this.invokeService = invokeService;
		return this;
	}

	public long respTimeoutMills() {
		return respTimeoutMills;
	}

	public BrokerPushContext respTimeoutMills(long respTimeoutMills) {
		this.respTimeoutMills = respTimeoutMills;
		return this;
	}

	public int pushMaxAttempt() {
		return pushMaxAttempt;
	}

	public BrokerPushContext pushMaxAttempt(int pushMaxAttempt) {
		this.pushMaxAttempt = pushMaxAttempt;
		return this;
	}

	public Map<String, String> channelGroupMap() {
		return channelGroupMap;
	}

	public BrokerPushContext channelGroupMap(Map<String, String> channelGroupMap) {
		this.channelGroupMap = channelGroupMap;
		return this;
	}
}
