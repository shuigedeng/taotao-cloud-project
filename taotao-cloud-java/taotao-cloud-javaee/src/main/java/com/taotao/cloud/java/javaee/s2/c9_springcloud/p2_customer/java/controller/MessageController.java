package com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.controller;

import com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.entity.Customer;
import com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.stream.StreamClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @Autowired
    private StreamClient streamClient;

    @GetMapping("/send")
    public String send(){
        streamClient.output().send(MessageBuilder.withPayload(new Customer(1,"张三",23)).build());
        return "消息发送成功！！";
    }
}
