package com.taotao.cloud.core.mq;


/**
 * 消息模型
 */
public class Message {

	/**
	 * 命名空间
	 */
	private String namespace;

	/**
	 * 主题
	 */
	private String topic;

	/**
	 * 分区/队列
	 */
	private Integer partition;

	/**
	 * 分区键
	 */
	private String key;

	/**
	 * 标签过滤
	 */
	private String tags;

	/**
	 * 消息体
	 */
	private String body;

	/**
	 * 延时等级
	 */
	private Integer delayTimeLevel = 0;

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Integer getPartition() {
		return partition;
	}

	public void setPartition(Integer partition) {
		this.partition = partition;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Integer getDelayTimeLevel() {
		return delayTimeLevel;
	}

	public void setDelayTimeLevel(Integer delayTimeLevel) {
		this.delayTimeLevel = delayTimeLevel;
	}
}
