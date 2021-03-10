package com.taotao.cloud.java.javaee.s2.c9_springcloud.p3_search.java.listener;

import com.rabbitmq.client.Channel;
import com.taotao.cloud.java.javaee.s2.c9_springcloud.p3_search.java.stream.StreamClient;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableBinding(StreamClient.class)
public class StreamReceiver {

    @StreamListener("myMessage")
    public void msg(Object msg,
                    @Header(name = AmqpHeaders.CHANNEL) Channel channel,
                    @Header(name = AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        System.out.println("接收到消息： " + msg);
        channel.basicAck(deliveryTag,false);
    }

}
