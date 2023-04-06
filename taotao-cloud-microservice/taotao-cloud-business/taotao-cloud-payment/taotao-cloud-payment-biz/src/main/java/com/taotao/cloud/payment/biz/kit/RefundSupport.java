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

package com.taotao.cloud.payment.biz.kit;

import com.taotao.cloud.order.api.feign.IFeignOrderApi;
import com.taotao.cloud.order.api.feign.IFeignOrderItemApi;
import com.taotao.cloud.order.api.feign.IFeignStoreFlowApi;
import com.taotao.cloud.order.api.model.vo.aftersale.AfterSaleVO;
import com.taotao.cloud.order.api.model.vo.order.OrderItemVO;
import com.taotao.cloud.order.api.model.vo.order.OrderVO;
import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import com.taotao.cloud.payment.biz.entity.RefundLog;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 退款支持
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-02 14:59:13
 */
@Component
public class RefundSupport {

    /** 店铺流水 */
    @Autowired
    private IFeignStoreFlowApi storeFlowApi;
    /** 订单 */
    @Autowired
    private IFeignOrderApi orderApi;
    /** 子订单 */
    @Autowired
    private IFeignOrderItemApi orderItemApi;

    /**
     * 售后退款
     *
     * @param afterSale
     */
    public void refund(AfterSaleVO afterSale) {
        OrderVO order = orderApi.getBySn(afterSale.orderSn());
        RefundLog refundLog = RefundLog.builder()
                .isRefund(false)
                .totalAmount(afterSale.getActualRefundPrice())
                .payPrice(afterSale.getActualRefundPrice())
                .memberId(afterSale.getMemberId())
                .paymentName(order.getPaymentMethod())
                .afterSaleNo(afterSale.getSn())
                .paymentReceivableNo(order.getReceivableNo())
                .outOrderNo("AF" + SnowFlake.getIdStr())
                .orderSn(afterSale.getOrderSn())
                .refundReason(afterSale.getReason())
                .build();

        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.paymentNameOf(order.getPaymentMethod());
        Payment payment = (Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());
        payment.refund(refundLog);

        this.updateReturnGoodsNumber(afterSale);

        // 记录退款流水
        storeFlowApi.refundOrder(afterSale);
    }

    /** 功能描述: 修改子订单中已售后退款商品数量 */
    private void updateReturnGoodsNumber(AfterSaleVO afterSale) {
        // 根据商品id及订单sn获取子订单
        OrderItemVO orderItem = orderItemApi.getByOrderSnAndSkuId(afterSale.orderSn(), afterSale.skuId());

        orderItem.setReturnGoodsNumber(afterSale.getNum() + orderItem.getReturnGoodsNumber());

        // 修改子订单订单中的退货数量
        orderItemApi.updateById(orderItem);
    }

    /** 订单取消 */
    public void cancel(AfterSaleVO afterSale) {
        OrderVO order = orderApi.getBySn(afterSale.orderSn());
        RefundLog refundLog = RefundLog.builder()
                .isRefund(false)
                .totalAmount(afterSale.getActualRefundPrice())
                .payPrice(afterSale.getActualRefundPrice())
                .memberId(afterSale.getMemberId())
                .paymentName(order.getPaymentMethod())
                .afterSaleNo(afterSale.getSn())
                .paymentReceivableNo(order.getReceivableNo())
                .outOrderNo("AF" + SnowFlake.getIdStr())
                .orderSn(afterSale.getOrderSn())
                .refundReason(afterSale.getReason())
                .build();
        PaymentMethodEnum paymentMethodEnum = PaymentMethodEnum.paymentNameOf(order.getPaymentMethod());
        Payment payment = (Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());
        payment.refund(refundLog);
    }

    /**
     * 退款通知
     *
     * @param paymentMethodEnum 支付渠道
     * @param request 请求
     * @since 2022-06-02 14:59:26
     */
    public void notify(PaymentMethodEnum paymentMethodEnum, HttpServletRequest request) {
        // 获取支付插件
        Payment payment = (Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());
        payment.refundNotify(request);
    }
}
