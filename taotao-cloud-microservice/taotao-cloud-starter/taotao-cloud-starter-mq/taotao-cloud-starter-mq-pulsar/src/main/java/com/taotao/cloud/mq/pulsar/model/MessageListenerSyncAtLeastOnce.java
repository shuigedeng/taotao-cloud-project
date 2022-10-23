package com.taotao.cloud.mq.pulsar.model;

import com.taotao.cloud.common.utils.log.LogUtils;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;

public class MessageListenerSyncAtLeastOnce<T> implements MessageListener<T> {

	@Override
	public void received(Consumer<T> consumer, Message<T> msg) {
		try {
			final boolean result = syncPayload(msg.getData());
			if (result) {
				consumer.acknowledgeAsync(msg);
			} else {
				consumer.negativeAcknowledge(msg);
			}
		} catch (Exception e) {
			// 业务方法可能会抛出异常
			LogUtils.error("exception is ", e);
			consumer.negativeAcknowledge(msg);
		}
	}

	/**
	 * 模拟同步执行的业务方法
	 *
	 * @param msg 消息体内容
	 * @return 发送是否成功
	 */
	private boolean syncPayload(byte[] msg) {
		return System.currentTimeMillis() % 2 == 0;
	}

}
