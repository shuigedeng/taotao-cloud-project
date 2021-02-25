package com.taotao.cloud.demo.rocketmq.transactional.service.impl;

import com.taotao.cloud.demo.rocketmq.transactional.model.Order;
import com.taotao.cloud.demo.rocketmq.transactional.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zlt
 */
@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {
    @Override
    public void save(Order order) {
        System.out.println("============保存订单成功：" + order.getOrderId());
    }
}
