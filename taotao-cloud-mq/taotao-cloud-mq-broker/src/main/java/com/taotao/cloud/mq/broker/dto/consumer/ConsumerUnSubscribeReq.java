package com.taotao.cloud.mq.broker.dto.consumer;


import com.taotao.cloud.mq.common.dto.req.MqCommonReq;

/**
 * 消费者注销入参
 *
 * @author shuigedeng
 * @since 2024.05
 */
public class ConsumerUnSubscribeReq extends MqCommonReq {

	/**
	 * 分组名称
	 *
	 * @since 2024.05
	 */
	private String groupName;

	/**
	 * 标题名称
	 */
	private String topicName;

	/**
	 * 标签正则
	 */
	private String tagRegex;

	/**
	 * 消费者类型
	 *
	 * @since 2024.05
	 */
	private String consumerType;

	public String getConsumerType() {
		return consumerType;
	}

	public void setConsumerType(String consumerType) {
		this.consumerType = consumerType;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getTagRegex() {
		return tagRegex;
	}

	public void setTagRegex(String tagRegex) {
		this.tagRegex = tagRegex;
	}

}
