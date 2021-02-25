package com.taotao.cloud.demo.rocketmq.transactional.service;


import com.taotao.cloud.demo.rocketmq.transactional.model.Order;

/**
* @author zlt
 */
public interface IOrderService {
    void save(Order order);
}
