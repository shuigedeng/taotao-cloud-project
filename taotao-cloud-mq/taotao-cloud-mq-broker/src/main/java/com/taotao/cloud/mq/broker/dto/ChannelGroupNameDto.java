package com.taotao.cloud.mq.broker.dto;

import io.netty.channel.Channel;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class ChannelGroupNameDto {

	/**
	 * 分组名称
	 */
	private String consumerGroupName;

	/**
	 * 通道
	 */
	private Channel channel;

	public static ChannelGroupNameDto of(String consumerGroupName,
		Channel channel) {
		ChannelGroupNameDto dto = new ChannelGroupNameDto();
		dto.setConsumerGroupName(consumerGroupName);
		dto.setChannel(channel);
		return dto;
	}

	public String getConsumerGroupName() {
		return consumerGroupName;
	}

	public void setConsumerGroupName(String consumerGroupName) {
		this.consumerGroupName = consumerGroupName;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
}
