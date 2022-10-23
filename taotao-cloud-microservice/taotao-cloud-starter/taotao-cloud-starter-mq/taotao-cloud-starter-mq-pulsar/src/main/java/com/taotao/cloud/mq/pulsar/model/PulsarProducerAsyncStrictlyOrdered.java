package com.taotao.cloud.mq.pulsar.model;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.concurrent.CompletableFuture;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;

public class PulsarProducerAsyncStrictlyOrdered {

	Producer<byte[]> producer;

	public void sendMsgAsync(byte[] msg, CompletableFuture<MessageId> future) {
		try {
			producer.sendAsync(msg).whenCompleteAsync((messageId, throwable) -> {
				if (throwable != null) {
					LogUtils.info("send success, id is {}", messageId);
					future.complete(messageId);
					return;
				}
				PulsarProducerAsyncStrictlyOrdered.this.sendMsgAsync(msg, future);
			});
		} catch (Exception e) {
			LogUtils.error("exception is ", e);
		}
	}

}
