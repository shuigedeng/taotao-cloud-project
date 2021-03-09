package com.taotao.cloud.java.javaee.s2.c9_springcloud.p3_search.java.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface StreamClient {
    @Input("myMessage")
    SubscribableChannel input();
}
