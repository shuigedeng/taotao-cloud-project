package com.taotao.cloud.bigdata.azkaban.mq.rabbitmq;

import com.free.bsf.core.util.JsonUtils;
import com.free.bsf.core.util.LogUtils;
import com.free.bsf.mq.base.*;
import com.free.bsf.mq.rocketmq.RocketMQConsumerProvider;
import com.free.bsf.mq.rocketmq.RocketMQProperties;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.io.IOException;


/**
 * $RabbitMQConsumerProvider  消费消息提供者组件
 *
 * @author clz.xu
 */
@Slf4j
public class RabbitMQConsumerProvider extends AbstractConsumerProvider {

    protected final ConnectionFactory connectionFactory;

    public RabbitMQConsumerProvider(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    @Override
    public <T> AbstractConsumer subscribe(AbstractSubscribeRunable<T> subscribeRunable) {
        Connection connection=null;
        Channel channel=null;
        try {
            connection =connectionFactory.createConnection();
            channel=connection.createChannel(false);
            // 消费消息
            channel.basicConsume(subscribeRunable.getQueueName(), false, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body)
                        throws IOException {
                    String routingKey = envelope.getRoutingKey();
                    String exchange = envelope.getExchange();
                    long deliveryTag = envelope.getDeliveryTag();
                    String messageId = properties.getMessageId();
                    String message = new String(body);
                    try {
                        RabbitMQMonitor.hook().run("rabbitmq-consume", () -> {
                            if (subscribeRunable.getType() == String.class) {
                                subscribeRunable.getRunnable().run(new Message<T>(messageId, exchange, routingKey, (T) message));
                            } else {
                                subscribeRunable.getRunnable().run(new Message<T>(messageId, exchange, routingKey, JsonUtils.deserialize(message, subscribeRunable.getType())));
                            }
                        });
                        this.getChannel().basicAck(deliveryTag, false);
                    } catch (Exception ex) {
                        this.getChannel().basicReject(envelope.getDeliveryTag(), false);
                        LogUtils.error(RabbitMQConsumerProvider.class, RabbitMQProperties.Project, "消费者初始化失败", ex);
                        throw new MQException(ex);
                    }
                }
            });
            AbstractConsumer abstractConsumer = new AbstractConsumer();
            abstractConsumer.setObject(channel);
            return abstractConsumer;
        } catch (IOException ex) {
            LogUtils.info(RocketMQConsumerProvider.class, RocketMQProperties.Project, String.format("rabbitmq 消费者队列%s 启动失败", subscribeRunable.getQueueName()));
            // 释放资源
            if(connection!=null){
                try{connection.close();}catch (Exception e){}
            }
            if(channel!=null){
                try{channel.close();}catch (Exception e){}
            }
            throw new MQException(ex);
        }
    }
}
