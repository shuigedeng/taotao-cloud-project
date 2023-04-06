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

package com.taotao.cloud.payment.biz.bootx.core.pay.builder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayChannelCode;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayStatusCode;
import com.taotao.cloud.payment.biz.bootx.core.pay.local.AsyncPayInfoLocal;
import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import com.taotao.cloud.payment.biz.bootx.dto.pay.PayResult;
import com.taotao.cloud.payment.biz.bootx.dto.pay.PaymentInfo;
import com.taotao.cloud.payment.biz.bootx.dto.payment.PayChannelInfo;
import com.taotao.cloud.payment.biz.bootx.dto.payment.RefundableInfo;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayModeParam;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayParam;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;

/**
 * 支付对象构建器
 *
 * @author xxm
 * @date 2021/2/25
 */
@UtilityClass
public class PaymentBuilder {

    /** 构建payment记录 */
    public Payment buildPayment(PayParam payParam) {
        Payment payment = new Payment();

        HttpServletRequest request = WebServletUtil.getRequest();
        String ip = ServletUtil.getClientIP(request);
        // 基础信息
        payment.setBusinessId(payParam.getBusinessId())
                .setUserId(payParam.getUserId())
                .setTitle(payParam.getTitle())
                .setDescription(payParam.getDescription());

        // 支付方式和状态
        List<PayChannelInfo> payTypeInfos = buildPayTypeInfo(payParam.getPayModeList());
        List<RefundableInfo> refundableInfos = buildRefundableInfo(payParam.getPayModeList());
        // 计算总价
        BigDecimal sumAmount = payTypeInfos.stream()
                .map(PayChannelInfo::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        // 支付通道信息
        payment.setPayChannelInfo(JSONUtil.toJsonStr(payTypeInfos))
                .setRefundableInfo(JSONUtil.toJsonStr(refundableInfos))
                .setPayStatus(PayStatusCode.TRADE_PROGRESS)
                .setAmount(sumAmount)
                .setClientIp(ip)
                .setRefundableBalance(sumAmount);
        return payment;
    }

    /** 构建PayTypeInfo */
    private List<PayChannelInfo> buildPayTypeInfo(List<PayModeParam> payModeParamList) {
        return CollectionUtil.isEmpty(payModeParamList)
                ? Collections.emptyList()
                : payModeParamList.stream().map(PayModeParam::toPayTypeInfo).collect(Collectors.toList());
    }
    /** 构建RefundableInfo */
    private List<RefundableInfo> buildRefundableInfo(List<PayModeParam> payModeParamList) {
        return CollectionUtil.isEmpty(payModeParamList)
                ? Collections.emptyList()
                : payModeParamList.stream().map(PayModeParam::toRefundableInfo).collect(Collectors.toList());
    }

    /** 根据Payment构建PayParam支付参数 */
    public PayParam buildPayParamByPayment(Payment payment) {
        PayParam payParam = new PayParam();
        // 恢复 payModeList
        List<PayModeParam> payModeParams = payment.getPayChannelInfoList().stream()
                .map(payTypeInfo -> new PayModeParam()
                        .setAmount(payTypeInfo.getAmount())
                        .setPayChannel(payTypeInfo.getPayChannel())
                        .setExtraParamsJson(payTypeInfo.getExtraParamsJson()))
                .collect(Collectors.toList());
        payParam.setPayModeList(payModeParams)
                .setBusinessId(payment.getBusinessId())
                .setUserId(payment.getUserId())
                .setTitle(payment.getTitle())
                .setTitle(payment.getTitle())
                .setDescription(payment.getDescription());
        return payParam;
    }

    /**
     * 根据Payment构建PaymentResult
     *
     * @param payment payment
     * @return paymentVO
     */
    public PayResult buildResultByPayment(Payment payment) {
        PayResult paymentResult;
        try {
            paymentResult = new PayResult();
            // 异步支付信息
            paymentResult
                    .setAsyncPayChannel(payment.getAsyncPayChannel())
                    .setAsyncPayMode(payment.isAsyncPayMode())
                    .setPayStatus(payment.getPayStatus())
                    .setPayment(buildPaymentInfo(payment));

            List<PayChannelInfo> channelInfos = payment.getPayChannelInfoList();

            // 设置异步支付参数
            List<PayChannelInfo> moneyPayTypeInfos = channelInfos.stream()
                    .filter(payTypeInfo -> PayChannelCode.ASYNC_TYPE.contains(payTypeInfo.getPayChannel()))
                    .collect(Collectors.toList());
            if (!CollUtil.isEmpty(moneyPayTypeInfos)) {
                paymentResult.setAsyncPayInfo(AsyncPayInfoLocal.get());
            }
            // 清空线程变量
        } finally {
            AsyncPayInfoLocal.clear();
        }
        return paymentResult;
    }

    /** 构建PaymentInfo */
    public PaymentInfo buildPaymentInfo(Payment payment) {
        PaymentInfo paymentInfo = new PaymentInfo();
        BeanUtils.copyProperties(payment, paymentInfo);
        return paymentInfo;
    }
}
