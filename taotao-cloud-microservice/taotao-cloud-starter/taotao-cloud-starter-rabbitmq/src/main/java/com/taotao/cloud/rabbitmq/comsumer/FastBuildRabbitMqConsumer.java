/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.rabbitmq.comsumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.ShutdownSignalException;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.rabbitmq.common.DetailResponse;
import com.taotao.cloud.rabbitmq.common.FastOcpRabbitMqConstants;
import com.taotao.cloud.rabbitmq.producer.MessageProcess;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * FastBuildRabbitMqConsumer
 *
 * @author shuigedeng
 * @since 2020/5/28 17:26
 */
@SuppressWarnings("all")
public class FastBuildRabbitMqConsumer {

	private ConnectionFactory connectionFactory;

	public FastBuildRabbitMqConsumer(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public <T> MessageConsumer buildMessageConsumer(String exchange, String routingKey,
		final String queue,
		final MessageProcess<T> messageProcess, String type) throws IOException {
		final Connection connection = connectionFactory.createConnection();

		//1 创建连接和channel
		buildQueue(exchange, routingKey, queue, connection, type);

		//2 设置message序列化方法
		final MessagePropertiesConverter messagePropertiesConverter = new DefaultMessagePropertiesConverter();
		final MessageConverter messageConverter = new Jackson2JsonMessageConverter();

		//3 consume
		return new MessageConsumer() {
			Channel channel;

			{
				channel = connection.createChannel(false);
			}

			@SuppressWarnings("unchecked")
			@Override
			public DetailResponse consume() {
				try {
					//1 通过basicGet获取原始数据
					GetResponse response = channel.basicGet(queue, false);

					while (response == null) {
						response = channel.basicGet(queue, false);
						Thread.sleep(FastOcpRabbitMqConstants.ONE_SECOND);
					}

					Message message = new Message(response.getBody(),
						messagePropertiesConverter
							.toMessageProperties(response.getProps(), response.getEnvelope(),
								"UTF-8"));
					//2 将原始数据转换为特定类型的包
					T messageBean = (T) messageConverter.fromMessage(message);

					//3 处理数据
					DetailResponse detailRes;

					try {
						detailRes = messageProcess.process(messageBean);
					} catch (Exception e) {
						LogUtils.error("exception", e);
						detailRes = new DetailResponse(false, "process exception: " + e, "");
					}

					//4 手动发送ack确认
					if (detailRes.isIfSuccess()) {
						channel.basicAck(response.getEnvelope().getDeliveryTag(), false);
					} else {
						//避免过多失败log
						Thread.sleep(FastOcpRabbitMqConstants.ONE_SECOND);
						LogUtils.info("process message failed: " + detailRes.getErrMsg());
						channel.basicNack(response.getEnvelope().getDeliveryTag(), false, true);
					}

					return detailRes;
				} catch (InterruptedException e) {
					LogUtils.error("exception", e);
					return new DetailResponse(false, "interrupted exception " + e.toString(), "");
				} catch (ShutdownSignalException | ConsumerCancelledException | IOException e) {
					LogUtils.error("exception", e);

					try {
						channel.close();
					} catch (IOException | TimeoutException ex) {
						LogUtils.error("exception", ex);
					}
					channel = connection.createChannel(false);
					return new DetailResponse(false,
						"shutdown or cancelled exception " + e.toString(), "");
				} catch (Exception e) {
					LogUtils.info("exception : ", e);
					try {
						channel.close();
					} catch (IOException | TimeoutException ex) {
						ex.printStackTrace();
					}
					channel = connection.createChannel(false);
					return new DetailResponse(false, "exception " + e.toString(), "");
				}
			}
		};
	}


	private void buildQueue(String exchange, String routingKey,
		final String queue, Connection connection, String type) throws IOException {
		Channel channel = connection.createChannel(false);

		if (type.equals("direct")) {
			channel.exchangeDeclare(exchange, "direct", true, false, null);
		} else if (type.equals("topic")) {
			channel.exchangeDeclare(exchange, "topic", true, false, null);
		}

		channel.queueDeclare(queue, true, false, false, null);
		channel.queueBind(queue, exchange, routingKey);

		try {
			channel.close();
		} catch (TimeoutException e) {
			LogUtils.info("close channel time out ", e);
		}
	}
}
