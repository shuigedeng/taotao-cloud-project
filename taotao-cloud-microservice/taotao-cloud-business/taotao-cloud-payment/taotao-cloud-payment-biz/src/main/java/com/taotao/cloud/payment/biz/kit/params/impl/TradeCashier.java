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

package com.taotao.cloud.payment.biz.kit.params.impl;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.api.feign.IFeignOrderApi;
import com.taotao.cloud.order.api.feign.IFeignTradeApi;
import com.taotao.cloud.order.api.model.vo.order.OrderVO;
import com.taotao.cloud.order.api.model.vo.trade.TradeVO;
import com.taotao.cloud.payment.api.enums.CashierEnum;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import com.taotao.cloud.payment.biz.kit.dto.PaymentSuccessParams;
import com.taotao.cloud.payment.biz.kit.params.CashierExecute;
import com.taotao.cloud.payment.biz.kit.params.dto.CashierParam;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingApi;
import com.taotao.cloud.sys.api.model.vo.setting.BaseSetting;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 整笔交易信息获取 */
@Component
public class TradeCashier implements CashierExecute {

    /** 交易 */
    @Autowired
    private IFeignTradeApi tradeApi;
    /** 订单 */
    @Autowired
    private IFeignOrderApi orderApi;
    /** 设置 */
    @Autowired
    private IFeignSettingApi settingApi;

    @Override
    public CashierEnum cashierEnum() {
        return CashierEnum.TRADE;
    }

    @Override
    public CashierParam getPaymentParams(PayParam payParam) {
        if (payParam.getOrderType().equals(CashierEnum.TRADE.name())) {
            // 准备返回的数据
            CashierParam cashierParam = new CashierParam();
            // 订单信息获取
            TradeVO trade = tradeApi.getBySn(payParam.getSn());

            List<OrderVO> orders = orderApi.getByTradeSn(payParam.getSn());

            String orderSns = orders.stream().map(OrderVO::getSn).collect(Collectors.joining(", "));
            cashierParam.setOrderSns(orderSns);

            for (OrderVO order : orders) {
                // 如果订单已支付，则不能发器支付
                if (order.orderBase().payStatus().equals(PayStatusEnum.PAID.name())) {
                    throw new BusinessException(ResultEnum.PAY_PARTIAL_ERROR);
                }
                // 如果订单状态不是待付款，则抛出异常
                if (!order.orderBase().payStatus().equals(OrderStatusEnum.UNPAID.name())) {
                    throw new BusinessException(ResultEnum.PAY_BAN);
                }
            }

            cashierParam.setPrice(trade.getFlowPrice());

            try {
                BaseSetting baseSetting = settingApi.getBaseSetting(SettingCategoryEnum.BASE_SETTING.name());
                cashierParam.setTitle(baseSetting.getSiteName());
            } catch (Exception e) {
                cashierParam.setTitle("多用户商城，在线支付");
            }
            String subject = "在线支付";
            cashierParam.setDetail(subject);

            cashierParam.setCreateTime(trade.getCreateTime());
            return cashierParam;
        }

        return null;
    }

    @Override
    public void paymentSuccess(PaymentSuccessParams paymentSuccessParams) {
        if (paymentSuccessParams.getPayParam().getOrderType().equals(CashierEnum.TRADE.name())) {
            tradeApi.payTrade(
                    paymentSuccessParams.getPayParam().getSn(),
                    paymentSuccessParams.getPaymentMethod(),
                    paymentSuccessParams.getReceivableNo());

            LogUtils.info(
                    "交易{}支付成功,方式{},流水号{},",
                    paymentSuccessParams.getPayParam().getSn(),
                    paymentSuccessParams.getPaymentMethod(),
                    paymentSuccessParams.getReceivableNo());
        }
    }

    @Override
    public Boolean paymentResult(PayParam payParam) {

        if (payParam.getOrderType().equals(CashierEnum.TRADE.name())) {
            TradeVO trade = tradeApi.getBySn(payParam.getSn());
            if (trade != null) {
                return PayStatusEnum.PAID.name().equals(trade.getPayStatus());
            } else {
                throw new BusinessException(ResultEnum.PAY_NOT_EXIST_ORDER);
            }
        }
        return false;
    }
}
