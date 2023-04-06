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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.service;

import cn.hutool.json.JSONUtil;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayChannelCode;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayStatusCode;
import com.taotao.cloud.payment.biz.bootx.core.payment.dao.PaymentManager;
import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.dao.AliPaymentManager;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.entity.AliPayment;
import com.taotao.cloud.payment.biz.bootx.dto.payment.PayChannelInfo;
import com.taotao.cloud.payment.biz.bootx.dto.payment.RefundableInfo;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayFailureException;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayModeParam;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付宝支付记录
 *
 * @author xxm
 * @date 2021/2/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPaymentService {
    private final AliPaymentManager aliPaymentManager;
    private final PaymentManager paymentManager;

    /** 支付调起成功 更新 payment 中 异步支付类型信息 */
    public void updatePaySuccess(Payment payment, PayModeParam payModeParam) {
        payment.setAsyncPayMode(true).setAsyncPayChannel(PayChannelCode.ALI);
        // TODO 设置超时时间

        List<PayChannelInfo> payTypeInfos = payment.getPayChannelInfoList();
        List<RefundableInfo> refundableInfos = payment.getRefundableInfoList();
        // 清除已有的异步支付类型信息
        payTypeInfos.removeIf(payTypeInfo -> PayChannelCode.ASYNC_TYPE.contains(payTypeInfo.getPayChannel()));
        refundableInfos.removeIf(payTypeInfo -> PayChannelCode.ASYNC_TYPE.contains(payTypeInfo.getPayChannel()));
        // 更新支付宝支付类型信息
        payTypeInfos.add(new PayChannelInfo()
                .setPayChannel(PayChannelCode.ALI)
                .setPayWay(payModeParam.getPayWay())
                .setAmount(payModeParam.getAmount())
                .setExtraParamsJson(payModeParam.getExtraParamsJson()));
        payment.setPayChannelInfo(JSONUtil.toJsonStr(payTypeInfos));
        // 更新支付宝可退款类型信息
        refundableInfos.add(
                new RefundableInfo().setPayChannel(PayChannelCode.ALI).setAmount(payModeParam.getAmount()));
        payment.setRefundableInfo(JSONUtil.toJsonStr(refundableInfos));
    }

    /** 更新异步支付记录成功状态, 并创建支付宝支付记录 */
    public void updateAsyncSuccess(Long id, PayModeParam payModeParam, String tradeNo) {

        // 更新支付记录
        Payment payment = paymentManager.findById(id).orElseThrow(() -> new PayFailureException("支付记录不存在"));

        // 创建支付宝支付记录
        AliPayment aliPayment = new AliPayment();
        aliPayment
                .setTradeNo(tradeNo)
                .setPaymentId(payment.getId())
                .setAmount(payModeParam.getAmount())
                .setRefundableBalance(payModeParam.getAmount())
                .setBusinessId(payment.getBusinessId())
                .setUserId(payment.getUserId())
                .setPayStatus(PayStatusCode.TRADE_SUCCESS)
                .setPayTime(LocalDateTime.now());
        aliPaymentManager.save(aliPayment);
    }

    /** 取消状态 */
    public void updateClose(Long paymentId) {
        Optional<AliPayment> aliPaymentOptional = aliPaymentManager.findByPaymentId(paymentId);
        aliPaymentOptional.ifPresent(aliPayment -> {
            aliPayment.setPayStatus(PayStatusCode.TRADE_CANCEL);
            aliPaymentManager.updateById(aliPayment);
        });
    }

    /** 更新退款 */
    public void updatePayRefund(Long paymentId, BigDecimal amount) {
        Optional<AliPayment> aliPaymentOptional = aliPaymentManager.findByPaymentId(paymentId);
        aliPaymentOptional.ifPresent(payment -> {
            BigDecimal refundableBalance = payment.getRefundableBalance().subtract(amount);
            payment.setRefundableBalance(refundableBalance);
            if (BigDecimalUtil.compareTo(refundableBalance, BigDecimal.ZERO) == 0) {
                payment.setPayStatus(PayStatusCode.TRADE_REFUNDED);
            } else {
                payment.setPayStatus(PayStatusCode.TRADE_REFUNDING);
            }
            aliPaymentManager.updateById(payment);
        });
    }
}
