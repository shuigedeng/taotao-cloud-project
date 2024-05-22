package com.taotao.cloud.mq.client.producer.dto;


import com.taotao.cloud.mq.client.producer.constant.SendStatus;
import java.util.List;

/**
 * 批量发送结果
 *
 * @author shuigedeng
 * @since 2024.05
 */
public class SendBatchResult {

	/**
	 * 消息唯一标识
	 */
	private List<String> messageIds;

	/**
	 * 发送状态
	 */
	private SendStatus status;

	public static SendBatchResult of(List<String> messageIds, SendStatus status) {
		SendBatchResult result = new SendBatchResult();
		result.setMessageIds(messageIds);
		result.setStatus(status);

		return result;
	}

	public List<String> getMessageIds() {
		return messageIds;
	}

	public void setMessageIds(List<String> messageIds) {
		this.messageIds = messageIds;
	}

	public SendStatus getStatus() {
		return status;
	}

	public void setStatus(SendStatus status) {
		this.status = status;
	}
}
