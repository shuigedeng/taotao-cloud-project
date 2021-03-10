package com.taotao.cloud.java.javaee.s2.c11_distributed.order.java.controller;

import com.taotao.cloud.java.javaee.s2.c11_distributed.order.java.service.OrderServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderServer orderServer;

    @GetMapping("/order")
    public String create(){
        orderServer.createOrder();

        return "创建订单成功！！";
    }

}
