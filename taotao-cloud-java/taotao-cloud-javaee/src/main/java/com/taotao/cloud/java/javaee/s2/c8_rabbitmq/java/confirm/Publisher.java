package com.taotao.cloud.java.javaee.s2.c8_rabbitmq.java.confirm;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.AMQBasicProperties;
import com.taotao.cloud.java.javaee.s2.c8_rabbitmq.java.config.RabbitMQClient;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class Publisher {
    @Test
    public void publish() throws Exception {
        //1. 获取Connection
        Connection connection = RabbitMQClient.getConnection();

        //2. 创建Channel
        Channel channel = connection.createChannel();


        // 开启return机制
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 当消息没有送达到queue时，才会执行。
                System.out.println(new String(body,"UTF-8") + "没有送达到Queue中！！");
            }
        });


        //3. 发布消息到exchange，同时指定路由的规则
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {

            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息发送成功，标识：" + deliveryTag + ",是否是批量" + multiple);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息发送失败，标识：" + deliveryTag + ",是否是批量" + multiple);
            }
        });

        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .deliveryMode(1)     //指定消息书否需要持久化 1 - 需要持久化  2 - 不需要持久化
                .messageId(UUID.randomUUID().toString())
                .build();
        String msg = "Hello-World！";
        channel.basicPublish("","HelloWorld",true,properties,msg.getBytes());








        System.in.read();
        System.out.println("生产者发布消息成功！");
        //4. 释放资源
        channel.close();
        connection.close();
    }

}
