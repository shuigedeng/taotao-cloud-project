package com.taotao.cloud.mq.client.consumer.support.listener;


import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.mq.client.consumer.api.IMqConsumerListener;
import com.taotao.cloud.mq.client.consumer.api.IMqConsumerListenerContext;
import com.taotao.cloud.mq.common.dto.req.MqMessage;
import com.taotao.cloud.mq.common.resp.ConsumerStatus;
import javax.annotation.concurrent.NotThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author shuigedeng
 * @since 2024.05
 */
@NotThreadSafe
public class MqListenerService implements IMqListenerService {

	private static final Logger LOG = LoggerFactory.getLogger(MqListenerService.class);

	private IMqConsumerListener mqConsumerListener;

	@Override
	public void register(IMqConsumerListener listener) {
		this.mqConsumerListener = listener;
	}

	@Override
	public ConsumerStatus consumer(MqMessage mqMessage, IMqConsumerListenerContext context) {
		if (mqConsumerListener == null) {
			LOG.warn("当前监听类为空，直接忽略处理。message: {}", JSON.toJSON(mqMessage));
			return ConsumerStatus.SUCCESS;
		}
		else {
			return mqConsumerListener.consumer(mqMessage, context);
		}
	}
}
