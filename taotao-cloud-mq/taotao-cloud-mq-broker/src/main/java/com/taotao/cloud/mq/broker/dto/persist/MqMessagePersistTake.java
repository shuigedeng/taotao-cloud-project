package com.taotao.cloud.mq.broker.dto.persist;

import java.util.List;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqMessagePersistTake {

	/**
	 * 分组名称
	 */
	private String groupName;

	/**
	 * 标题名称
	 */
	private String topic;

	/**
	 * 标签
	 */
	private List<String> tags;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
