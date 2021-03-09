package com.taotao.cloud.java.javaee.s2.c8_rabbitmq.java.helloworld;

import com.qf.config.RabbitMQClient;
import com.rabbitmq.client.*;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;

public class Consumer {
    @Test
    public void consume() throws Exception {
        //1. 获取连接对象
        Connection connection = RabbitMQClient.getConnection();

        //2. 创建channel
        Channel channel = connection.createChannel();

        //3. 声明队列-HelloWorld
        //参数1：queue - 指定队列的名称
        //参数2：durable - 当前队列是否需要持久化（true）
        //参数3：exclusive - 是否排外（conn.close() - 当前队列会被自动删除，当前队列只能被一个消费者消费）
        //参数4：autoDelete - 如果这个队列没有消费者在消费，队列自动删除
        //参数5：arguments - 指定当前队列的其他信息
        channel.queueDeclare("HelloWorld",true,false,false,null);

        //4. 开启监听Queue
        DefaultConsumer consume = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                Jedis jedis = new Jedis("192.168.199.109",6379);
                String messageId = properties.getMessageId();
                //1. setnx到Redis中，默认指定value-0
                String result = jedis.set(messageId, "0", "NX", "EX", 10);
                if(result != null && result.equalsIgnoreCase("OK")) {
                    System.out.println("接收到消息：" + new String(body, "UTF-8"));
                    //2. 消费成功，set messageId 1
                    jedis.set(messageId,"1");
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }else {
                    //3. 如果1中的setnx失败，获取key对应的value，如果是0，return，如果是1
                    String s = jedis.get(messageId);
                    if("1".equalsIgnoreCase(s)){
                        channel.basicAck(envelope.getDeliveryTag(),false);
                    }
                }
            }
        };

        //参数1：queue - 指定消费哪个队列
        //参数2：autoAck - 指定是否自动ACK （true，接收到消息后，会立即告诉RabbitMQ）
        //参数3：consumer - 指定消费回调
        channel.basicConsume("HelloWorld",false,consume);

        System.out.println("消费者开始监听队列！");
        // System.in.read();
        System.in.read();

        //5. 释放资源
        channel.close();
        connection.close();
    }

}
