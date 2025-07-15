package com.taotao.cloud.mq.example.producer;


import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.mq.client.producer.core.MqProducer;
import com.taotao.cloud.mq.client.producer.dto.SendResult;
import com.taotao.cloud.mq.common.dto.req.MqMessage;
import io.netty.util.concurrent.DefaultPromise;

import java.util.Arrays;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class ProducerMain {

	public static void main(String[] args) {
		MqProducer mqProducer = new MqProducer();
		//mqProducer.appKey("test")
		//	.appSecret("mq");
		mqProducer.start();

		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		for (int i = 0; i < 20; i++) {
			MqMessage mqMessage = buildMessage(i);
			SendResult sendResult = mqProducer.send(mqMessage);
			System.out.println("========"+JSON.toJSON(sendResult));
		}
	}

	private static MqMessage buildMessage(int i) {
		String message = "HELLO MQ!" + i;
		MqMessage mqMessage = new MqMessage();
		mqMessage.setTopic("TOPIC");
		mqMessage.setTags(Arrays.asList("TAGA", "TAGB"));
		mqMessage.setPayload(message);

		return mqMessage;
	}

}
