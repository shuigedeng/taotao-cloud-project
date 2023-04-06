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

package com.taotao.cloud.payment.biz.bootx.core.pay.strategy;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import com.taotao.cloud.payment.biz.bootx.code.pay.PayChannelCode;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayChannelEnum;
import com.taotao.cloud.payment.biz.bootx.core.pay.func.AbsPayStrategy;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.cash.service.CashService;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayAmountAbnormalException;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayModeParam;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 现金支付
 *
 * @author xxm
 * @date 2021/6/23
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class CashPayStrategy extends AbsPayStrategy {
    private final CashService cashService;
    private final PaymentService paymentService;

    /** 现金支付 */
    @Override
    public int getType() {
        return PayChannelCode.CASH;
    }

    /** 支付前检查 */
    @Override
    public void doBeforePayHandler() {
        // 检查金额
        PayModeParam payMode = this.getPayMode();
        if (BigDecimalUtil.compareTo(payMode.getAmount(), BigDecimal.ZERO) < 1) {
            throw new PayAmountAbnormalException();
        }
    }
    /** 支付操作 */
    @Override
    public void doPayHandler() {
        cashService.pay(this.getPayMode(), this.getPayment(), this.getPayParam());
    }

    /** 关闭本地支付记录 */
    @Override
    public void doCloseHandler() {
        cashService.close(this.getPayment().getId());
    }

    /** 退款 */
    @Override
    public void doRefundHandler() {
        cashService.refund(this.getPayment().getId(), this.getPayMode().getAmount());
        paymentService.updateRefundSuccess(this.getPayment(), this.getPayMode().getAmount(), PayChannelEnum.CASH);
    }
}
