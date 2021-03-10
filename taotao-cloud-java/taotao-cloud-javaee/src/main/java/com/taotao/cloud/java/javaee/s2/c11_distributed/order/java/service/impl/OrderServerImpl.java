package com.taotao.cloud.java.javaee.s2.c11_distributed.order.java.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.taotao.cloud.java.javaee.s2.c11_distributed.order.java.mapper.OrderMapper;
import com.taotao.cloud.java.javaee.s2.c11_distributed.order.java.service.OrderServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderServerImpl implements OrderServer {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Transactional
    @LcnTransaction
    public void createOrder() {
    //        1. 减库存
        restTemplate.getForObject("http://localhost:8082/item",String.class);
        int i = 1/0;
    //        2. 创建订单
        orderMapper.save();
    }
}
