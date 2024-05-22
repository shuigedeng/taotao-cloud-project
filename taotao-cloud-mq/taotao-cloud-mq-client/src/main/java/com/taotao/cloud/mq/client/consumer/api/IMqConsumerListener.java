package com.taotao.cloud.mq.client.consumer.api;


import com.taotao.cloud.mq.common.dto.req.MqMessage;
import com.taotao.cloud.mq.common.resp.ConsumerStatus;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public interface IMqConsumerListener {


	/**
	 * 消费
	 *
	 * @param mqMessage 消息体
	 * @param context   上下文
	 * @return 结果
	 */
	ConsumerStatus consumer(final MqMessage mqMessage,
		final IMqConsumerListenerContext context);

}
