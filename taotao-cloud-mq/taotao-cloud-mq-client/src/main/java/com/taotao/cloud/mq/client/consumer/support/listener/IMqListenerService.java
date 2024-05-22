package com.taotao.cloud.mq.client.consumer.support.listener;


import com.taotao.cloud.mq.client.consumer.api.IMqConsumerListener;
import com.taotao.cloud.mq.client.consumer.api.IMqConsumerListenerContext;
import com.taotao.cloud.mq.common.dto.req.MqMessage;
import com.taotao.cloud.mq.common.resp.ConsumerStatus;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public interface IMqListenerService {

	/**
	 * 注册
	 *
	 * @param listener 监听器
	 * @since 2024.05
	 */
	void register(final IMqConsumerListener listener);

	/**
	 * 消费消息
	 *
	 * @param mqMessage 消息
	 * @param context   上下文
	 * @return 结果
	 * @since 2024.05
	 */
	ConsumerStatus consumer(final MqMessage mqMessage,
		final IMqConsumerListenerContext context);

}
