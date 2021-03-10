package com.taotao.cloud.java.javaee.s2.c8_rabbitmq.java.work;

import com.rabbitmq.client.*;
import com.taotao.cloud.java.javaee.s2.c8_rabbitmq.java.config.RabbitMQClient;
import org.junit.Test;

import java.io.IOException;

public class Consumer1 {
    @Test
    public void consume() throws Exception {
        //1. 获取连接对象
        Connection connection = RabbitMQClient.getConnection();

        //2. 创建channel
        Channel channel = connection.createChannel();

        //3. 声明队列-HelloWorld
        channel.queueDeclare("Work",true,false,false,null);

        //3.5 指定当前消费者，一次消费多少个消息
        channel.basicQos(1);

        //4. 开启监听Queue
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("消费者1号接收到消息：" + new String(body,"UTF-8"));

                // 手动ack
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        channel.basicConsume("Work",false,consumer);

        System.out.println("开始消费消息。。。。");
        // System.in.read();


        System.in.read();
        //5. 释放资源
        channel.close();
        connection.close();
    }

}
