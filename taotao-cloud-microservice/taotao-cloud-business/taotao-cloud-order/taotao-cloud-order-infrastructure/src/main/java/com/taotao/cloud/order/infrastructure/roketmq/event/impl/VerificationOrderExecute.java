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

package com.taotao.cloud.order.infrastructure.roketmq.event.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.taotao.cloud.order.api.enums.order.OrderComplaintStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderItemAfterSaleStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.sys.model.message.OrderMessage;
import com.taotao.cloud.order.infrastructure.model.entity.order.Order;
import com.taotao.cloud.order.infrastructure.model.entity.order.OrderItem;
import com.taotao.cloud.order.infrastructure.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.infrastructure.service.business.order.IOrderItemService;
import com.taotao.cloud.order.infrastructure.service.business.order.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 虚拟商品
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-19 15:12:25
 */
@Component
public class VerificationOrderExecute implements OrderStatusChangeEvent {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderItemService orderItemService;

    @Override
    public void orderChange(OrderMessage orderMessage) {
        // 订单状态为待核验，添加订单添加核验码
        if (orderMessage.getNewStatus().equals(OrderStatusEnum.TAKE)) {
            // 获取订单信息
            Order order = orderService.getBySn(orderMessage.getOrderSn());
            // 获取随机数，判定是否存在
            String code = getCode(order.getStoreId());
            // 设置订单验证码
            orderService.update(new LambdaUpdateWrapper<Order>()
                    .set(Order::getVerificationCode, code)
                    .eq(Order::getSn, orderMessage.getOrderSn()));
            // 修改虚拟订单货物可以进行售后、投诉
            orderItemService.update(new LambdaUpdateWrapper<OrderItem>()
                    .eq(OrderItem::getOrderSn, orderMessage.getOrderSn())
                    .set(OrderItem::getAfterSaleStatus, OrderItemAfterSaleStatusEnum.NOT_APPLIED)
                    .set(OrderItem::getCommentStatus, OrderComplaintStatusEnum.NO_APPLY));
        }
    }

    /**
     * 获取随机数 判断当前店铺下是否使用验证码，如果已使用则重新获取
     *
     * @param storeId 店铺ID
     * @return {@link String }
     * @since 2022-05-16 17:44:50
     */
    private String getCode(Long storeId) {
        // 获取八位验证码
        String code = Long.toString(RandomUtil.randomLong(10000000, 99999999));

        LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getVerificationCode, code)
                .eq(Order::getStoreId, storeId);

        if (orderService.getOne(lambdaQueryWrapper) == null) {
            return code;
        } else {
            return this.getCode(storeId);
        }
    }
}
