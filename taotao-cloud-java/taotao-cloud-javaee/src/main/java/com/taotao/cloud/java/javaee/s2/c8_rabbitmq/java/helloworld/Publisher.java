package com.taotao.cloud.java.javaee.s2.c8_rabbitmq.java.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.taotao.cloud.java.javaee.s2.c8_rabbitmq.java.config.RabbitMQClient;
import org.junit.Test;

import java.io.IOException;

public class Publisher {

    @Test
    public void publish() throws Exception {
        //1. 获取Connection
        Connection connection = RabbitMQClient.getConnection();

        //2. 创建Channel
        Channel channel = connection.createChannel();

        //3. 发布消息到exchange，同时指定路由的规则
        String msg = "Hello-World！";
        // 参数1：指定exchange，使用""。
        // 参数2：指定路由的规则，使用具体的队列名称。
        // 参数3：指定传递的消息所携带的properties，使用null。
        // 参数4：指定发布的具体消息，byte[]类型
        channel.basicPublish("","HelloWorld",null,msg.getBytes());
        // Ps：exchange是不会帮你将消息持久化到本地的，Queue才会帮你持久化消息。
        System.out.println("生产者发布消息成功！");
        //4. 释放资源
        channel.close();
        connection.close();
    }

}
