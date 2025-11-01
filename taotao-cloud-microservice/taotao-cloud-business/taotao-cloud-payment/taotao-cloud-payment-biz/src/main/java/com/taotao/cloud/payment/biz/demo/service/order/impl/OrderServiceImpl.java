/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.payment.biz.demo.service.order.impl;

import com.yungouos.springboot.demo.entity.Order;
import com.yungouos.springboot.demo.service.base.BaseService;
import com.yungouos.springboot.demo.service.order.OrderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class OrderServiceImpl extends BaseService<Order> implements OrderService {

    @Override
    public Order add(String body, String money) {
        Order order = null;
        try {
            order = new Order();
            order.setOrderNo(System.currentTimeMillis() + "");
            order.setBody(body);
            order.setMoney(money);
            order.setStatus(0);
            order.setAddTime(DateUtil.now());
            Integer i = super.save(order);
            if (i == null || i <= 0) {
                throw new Exception("订单保存失败");
            }
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return order;
    }

    @Override
    public Order getOrderInfo(String orderNo) {
        Order order = null;
        try {
            Order orderWhere = new Order();
            orderWhere.setOrderNo(orderNo);
            order = super.selectOne(orderWhere);
        } catch (Exception e) {
            LogUtils.error(e);
            return null;
        }
        return order;
    }

    @Override
    public boolean paySuccess(String orderNo, String payNo, String payTime) {
        try {
            Order order = getOrderInfo(orderNo);
            if (order == null) {
                throw new Exception("订单不存在");
            }
            if (order.getStatus().intValue() == 1) {
                return true;
            }

            Order orderData = new Order();
            orderData.setStatus(1);
            orderData.setPayNo(payNo);
            orderData.setPayTime(payTime);

            Example example = new Example(Order.class);
            Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orderNo", orderNo);
            super.updateSelective(orderData, example);
            return true;
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return false;
    }
}
