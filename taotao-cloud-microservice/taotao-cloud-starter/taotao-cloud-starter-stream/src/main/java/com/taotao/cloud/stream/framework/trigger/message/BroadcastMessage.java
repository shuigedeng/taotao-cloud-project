package com.taotao.cloud.stream.framework.trigger.message;

/**
 * 直播消息实体
 */
public class BroadcastMessage {

	/**
	 * 直播间ID
	 */
	private String studioId;

	/**
	 * 状态
	 */
	private String status;


	public BroadcastMessage(String studioId, String status) {
		this.studioId = studioId;
		this.status = status;
	}

	public String getStudioId() {
		return studioId;
	}

	public void setStudioId(String studioId) {
		this.studioId = studioId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
