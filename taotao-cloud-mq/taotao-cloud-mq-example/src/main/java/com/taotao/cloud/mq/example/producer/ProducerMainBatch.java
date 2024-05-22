package com.taotao.cloud.mq.example.producer;


import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.mq.client.producer.core.MqProducer;
import com.taotao.cloud.mq.client.producer.dto.SendBatchResult;
import com.taotao.cloud.mq.common.dto.req.MqMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class ProducerMainBatch {

	public static void main(String[] args) {
		MqProducer mqProducer = new MqProducer();
		mqProducer.start();

		List<MqMessage> mqMessageList = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			MqMessage mqMessage = buildMessage(i);
			mqMessageList.add(mqMessage);
		}

		SendBatchResult sendResult = mqProducer.sendBatch(mqMessageList);
		System.out.println(JSON.toJSON(sendResult));
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
