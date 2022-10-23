package com.taotao.cloud.mq.stream.framework.trigger.message;

/**
 * 直播消息实体
 */
public class BroadcastMessage {

	/**
	 * 直播间ID
	 */
	private Long studioId;

	/**
	 * 状态
	 */
	private String status;


	public BroadcastMessage(Long studioId, String status) {
		this.studioId = studioId;
		this.status = status;
	}

	public Long getStudioId() {
		return studioId;
	}

	public void setStudioId(Long studioId) {
		this.studioId = studioId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
