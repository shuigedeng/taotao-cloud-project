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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.service;

import cn.bootx.common.core.exception.BizException;
import cn.bootx.common.core.util.BigDecimalUtil;
import cn.bootx.payment.code.pay.PayStatusCode;
import cn.bootx.payment.core.payment.entity.Payment;
import cn.bootx.payment.core.paymodel.wallet.dao.WalletPaymentManager;
import cn.bootx.payment.core.paymodel.wallet.entity.Wallet;
import cn.bootx.payment.core.paymodel.wallet.entity.WalletPayment;
import cn.bootx.payment.param.pay.PayModeParam;
import cn.bootx.payment.param.pay.PayParam;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 钱包交易记录的相关操作
 *
 * @author xxm
 * @date 2020/12/8
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletPaymentService {
    private final WalletPaymentManager walletPaymentManager;

    /** 保存钱包支付记录 */
    public void savePayment(
            Payment payment, PayParam payParam, PayModeParam payMode, Wallet wallet) {
        WalletPayment walletPayment = new WalletPayment().setWalletId(wallet.getId());
        walletPayment
                .setPaymentId(payment.getId())
                .setUserId(payment.getUserId())
                .setBusinessId(payParam.getBusinessId())
                .setAmount(payMode.getAmount())
                .setRefundableBalance(payMode.getAmount())
                .setPayStatus(payment.getPayStatus());
        walletPaymentManager.save(walletPayment);
    }

    /** 更新成功状态 */
    public void updateSuccess(Long paymentId) {
        Optional<WalletPayment> payment = walletPaymentManager.findByPaymentId(paymentId);
        if (payment.isPresent()) {
            WalletPayment walletPayment = payment.get();
            walletPayment.setPayStatus(PayStatusCode.TRADE_SUCCESS).setPayTime(LocalDateTime.now());
            walletPaymentManager.updateById(walletPayment);
        }
    }

    /** 关闭操作 */
    public void updateClose(Long paymentId) {
        WalletPayment walletPayment =
                walletPaymentManager
                        .findByPaymentId(paymentId)
                        .orElseThrow(() -> new BizException("未查询到查询交易记录"));
        walletPayment.setPayStatus(PayStatusCode.TRADE_CANCEL);
        walletPaymentManager.updateById(walletPayment);
    }

    /** 更新退款 */
    public void updateRefund(Long paymentId, BigDecimal amount) {
        Optional<WalletPayment> walletPayment = walletPaymentManager.findByPaymentId(paymentId);
        walletPayment.ifPresent(
                payment -> {
                    BigDecimal refundableBalance = payment.getRefundableBalance().subtract(amount);
                    payment.setRefundableBalance(refundableBalance);
                    if (BigDecimalUtil.compareTo(refundableBalance, BigDecimal.ZERO) == 0) {
                        payment.setPayStatus(PayStatusCode.TRADE_REFUNDED);
                    } else {
                        payment.setPayStatus(PayStatusCode.TRADE_REFUNDING);
                    }
                    walletPaymentManager.updateById(payment);
                });
    }
}
