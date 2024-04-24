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

import com.egzosn.pay.paypal.bean.order.Payment;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.sys.model.message.OrderMessage;
import com.taotao.cloud.order.infrastructure.model.entity.order.Order;
import com.taotao.cloud.order.infrastructure.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.infrastructure.service.business.order.IOrderService;
import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-05-19 15:03:55
 */
@Service
public class PaymentExecute implements OrderStatusChangeEvent {

    /** 订单 */
    @Autowired
    private IOrderService orderService;

    @Override
    public void orderChange(OrderMessage orderMessage) {
        if (orderMessage.getNewStatus() == OrderStatusEnum.CANCELLED) {
            Order order = orderService.getBySn(orderMessage.getOrderSn());

            // 如果未付款，则不去要退回相关代码执行
            if (order.getPayStatus().equals(PayStatusEnum.UNPAID.name())) {
                return;
            }
            PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.valueOf(order.getPaymentMethod());

            // 获取支付方式
            Payment payment = (Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());

            RefundLog refundLog = RefundLog.builder()
                    .isRefund(false)
                    .totalAmount(order.getFlowPrice())
                    .payPrice(order.getFlowPrice())
                    .memberId(order.getMemberId())
                    .paymentName(order.getPaymentMethod())
                    .afterSaleNo("订单取消")
                    .orderSn(order.getSn())
                    .paymentReceivableNo(order.getReceivableNo())
                    .outOrderNo("AF" + SnowFlake.getIdStr())
                    .outOrderNo("AF" + SnowFlake.getIdStr())
                    .refundReason("订单取消")
                    .build();
            payment.refund(refundLog);
        }
    }
}
