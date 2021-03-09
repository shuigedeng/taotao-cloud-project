package com.taotao.cloud.java.javaee.s2.c8_rabbitmq.java.work;

import com.qf.config.RabbitMQClient;
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

        //3. 发布消息到exchange，同时指定路由的规则
        for (int i = 0; i < 10 ; i++) {
            String msg = "Hello-World！" + i;
            channel.basicPublish("","Work",null,msg.getBytes());
        }

        System.out.println("生产者发布消息成功！");
        //4. 释放资源
        channel.close();
        connection.close();
    }

}
