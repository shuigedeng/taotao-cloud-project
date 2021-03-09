package com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StreamClient {
    @Output("myMessage")
    MessageChannel output();

}
