package com.taotao.cloud.common.mq.producer;


/**
 * 消息发送结果
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class MessageSendResult {

	/**
	 * 主题
	 */
	private String topic;

	/**
	 * 分区
	 */
	private Integer partition;

	/**
	 * 偏移量
	 */
	private Long offset;

	/**
	 * 事务ID
	 */
	private String transactionId;

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

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
}
