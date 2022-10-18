package com.taotao.cloud.common.mq.producer;

/**
 * 消息发送回调接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
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
