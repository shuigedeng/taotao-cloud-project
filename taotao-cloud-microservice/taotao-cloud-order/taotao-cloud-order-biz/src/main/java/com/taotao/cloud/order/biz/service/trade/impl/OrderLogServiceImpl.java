package com.taotao.cloud.order.biz.service.trade.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.order.biz.entity.trade.OrderLog;
import com.taotao.cloud.order.biz.mapper.trade.OrderLogMapper;
import com.taotao.cloud.order.biz.service.trade.OrderLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单日志业务层实现
 *
 */
@Service
public class OrderLogServiceImpl extends ServiceImpl<OrderLogMapper, OrderLog> implements
	OrderLogService {

    @Override
    public List<OrderLog> getOrderLog(String orderSn) {
        LambdaQueryWrapper<OrderLog> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(OrderLog::getOrderSn, orderSn);
        return this.list(lambdaQueryWrapper);
    }
}
