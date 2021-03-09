package com.taotao.cloud.java.javaee.s2.c8_rabbitmq.java.topic;

import com.qf.config.RabbitMQClient;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;

public class Publisher {

    @Test
    public void publish() throws Exception {
        //1. 获取Connection
        Connection connection = RabbitMQClient.getConnection();

        //2. 创建Channel
        Channel channel = connection.createChannel();

        // 创建exchange绑定队列      topic-queue-1   topic-queue-2
        // 动物的信息 <speed>.<color>.<what>
        // *.red.*              -> *占位符
        // fast.#               -> #通配符
        // *.*.rabbit
        channel.exchangeDeclare("topic-exchange", BuiltinExchangeType.TOPIC);
        channel.queueBind("topic-queue-1","topic-exchange","*.red.*");
        channel.queueBind("topic-queue-2","topic-exchange","fast.#");
        channel.queueBind("topic-queue-2","topic-exchange","*.*.rabbit");

        //3. 发布消息到exchange，同时指定路由的规则
        channel.basicPublish("topic-exchange","fast.red.monkey",null,"红快猴子".getBytes());
        channel.basicPublish("topic-exchange","slow.black.dog",null,"黑漫狗".getBytes());
        channel.basicPublish("topic-exchange","fast.white.cat",null,"快白猫".getBytes());

        System.out.println("生产者发布消息成功！");
        //4. 释放资源
        channel.close();
        connection.close();
    }

}
