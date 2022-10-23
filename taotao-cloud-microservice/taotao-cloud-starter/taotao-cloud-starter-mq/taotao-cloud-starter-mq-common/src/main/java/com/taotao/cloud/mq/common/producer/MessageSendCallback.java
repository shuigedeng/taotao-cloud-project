package com.taotao.cloud.mq.common.producer;

/**
 * 消息发送回调接口
 */
public interface MessageSendCallback {

	/**
	 * 消息发送成功
	 *
	 * @param result
	 */
	void onSuccess(MessageSendResult result);

	/**
	 * 消息发送失败
	 *
	 * @param e
	 */
	void onFailed(Throwable e);
}
