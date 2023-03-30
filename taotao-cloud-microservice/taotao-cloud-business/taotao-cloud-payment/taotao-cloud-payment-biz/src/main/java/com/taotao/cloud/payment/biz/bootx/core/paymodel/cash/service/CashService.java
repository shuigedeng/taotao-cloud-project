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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.cash.service;

import com.taotao.cloud.payment.biz.bootx.code.pay.PayStatusCode;
import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.cash.dao.CashPaymentManager;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.cash.entity.CashPayment;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayModeParam;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayParam;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 现金支付
 *
 * @author xxm
 * @date 2021/6/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashService {
    private final CashPaymentManager cashPaymentManager;

    /** 支付 */
    public void pay(PayModeParam payMode, Payment payment, PayParam payParam) {
        CashPayment walletPayment = new CashPayment();
        walletPayment
                .setPaymentId(payment.getId())
                .setUserId(payment.getUserId())
                .setBusinessId(payParam.getBusinessId())
                .setAmount(payMode.getAmount())
                .setRefundableBalance(payMode.getAmount())
                .setPayStatus(payment.getPayStatus());
        cashPaymentManager.save(walletPayment);
    }

    /** 关闭 */
    public void close(Long paymentId) {
        Optional<CashPayment> cashPaymentOpt = cashPaymentManager.findByPaymentId(paymentId);
        cashPaymentOpt.ifPresent(
                cashPayment -> {
                    cashPayment.setPayStatus(PayStatusCode.TRADE_CANCEL);
                    cashPaymentManager.updateById(cashPayment);
                });
    }

    /** 退款 */
    public void refund(Long paymentId, BigDecimal amount) {
        Optional<CashPayment> cashPayment = cashPaymentManager.findByPaymentId(paymentId);
        cashPayment.ifPresent(
                payment -> {
                    BigDecimal refundableBalance = payment.getRefundableBalance().subtract(amount);
                    if (BigDecimalUtil.compareTo(refundableBalance, BigDecimal.ZERO) == 0) {
                        payment.setPayStatus(PayStatusCode.TRADE_REFUNDED);
                    } else {
                        payment.setPayStatus(PayStatusCode.TRADE_REFUNDING);
                    }
                    cashPaymentManager.updateById(payment);
                });
    }
}
