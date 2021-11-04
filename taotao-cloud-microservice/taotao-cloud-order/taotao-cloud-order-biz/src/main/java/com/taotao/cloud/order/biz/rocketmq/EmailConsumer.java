
package com.taotao.cloud.order.biz.rocketmq;

import com.taotao.cloud.rocketmq.channel.TaoTaoCloudSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@StreamListener(TaoTaoCloudSink.SMS_MESSAGE_INPUT)
	public void onMessage(@Payload String message) {
		logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
	}

	//@StreamListener(MySink.TREK_INPUT)
	//public void onTrekMessage(@Payload Demo01Message message) {
	//    logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
	//}
}
