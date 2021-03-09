package com.taotao.cloud.java.javaee.s2.c8_rabbitmq.java.pubsub;

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

        //3. 创建exchange - 绑定某一个队列
        //参数1： exchange的名称
        //参数2： 指定exchange的类型  FANOUT - pubsub ,   DIRECT - Routing , TOPIC - Topics
        channel.exchangeDeclare("pubsub-exchange", BuiltinExchangeType.FANOUT);
        channel.queueBind("pubsub-queue1","pubsub-exchange","");
        channel.queueBind("pubsub-queue2","pubsub-exchange","");

        //4. 发布消息到exchange，同时指定路由的规则
        for (int i = 0; i < 10 ; i++) {
            String msg = "Hello-World！" + i;
            channel.basicPublish("pubsub-exchange","",null,msg.getBytes());
        }

        System.out.println("生产者发布消息成功！");
        //5. 释放资源
        channel.close();
        connection.close();
    }

}
