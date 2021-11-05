
package com.taotao.cloud.order.biz.rocketmq;

import com.taotao.cloud.rocketmq.channel.TaoTaoCloudSink;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {


	@StreamListener(TaoTaoCloudSink.EMAIL_MESSAGE_INPUT)
	public void onMessage(@Payload String message) {
		//System.out.println(
		//	"[onMessage][线程编号:{} 消息内容：{}]" + Thread.currentThread().getId() + message);
		System.out.println("email Consumer" + message);
	}

	//@StreamListener(MySink.TREK_INPUT)
	//public void onTrekMessage(@Payload Demo01Message message) {
	//    logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
	//}
}
