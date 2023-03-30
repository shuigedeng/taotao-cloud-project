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
import com.taotao.cloud.payment.biz.bootx.core.payment.service.PaymentService;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.entity.Voucher;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.service.VoucherPayService;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.service.VoucherPaymentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 储值卡支付
 *
 * @author xxm
 * @date 2022/3/13
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class VoucherStrategy extends AbsPayStrategy {
    private final VoucherPayService voucherPayService;
    private final VoucherPaymentService voucherPaymentService;
    private final PaymentService paymentService;
    private List<Voucher> vouchers;

    @Override
    public int getType() {
        return PayChannelCode.VOUCHER;
    }

    /** 支付前处理 */
    @Override
    public void doBeforePayHandler() {
        // 获取并校验余额
        this.vouchers = voucherPayService.getAndCheckVoucher(this.getPayMode());
    }

    /** 支付操作 */
    @Override
    public void doPayHandler() {
        voucherPayService.pay(getPayMode().getAmount(), this.getPayment(), this.vouchers);
        voucherPaymentService.savePayment(getPayment(), getPayParam(), getPayMode(), vouchers);
    }

    /** 成功 */
    @Override
    public void doSuccessHandler() {
        voucherPaymentService.updateSuccess(this.getPayment().getId());
    }

    /** 关闭支付 */
    @Override
    public void doCloseHandler() {
        voucherPayService.close(this.getPayment().getId());
        voucherPaymentService.updateClose(this.getPayment().getId());
    }

    /** 退款 */
    @Override
    public void doRefundHandler() {
        voucherPayService.refund(this.getPayment().getId(), this.getPayMode().getAmount());
        voucherPaymentService.updateRefund(
                this.getPayment().getId(), this.getPayMode().getAmount());
        paymentService.updateRefundSuccess(
                this.getPayment(), this.getPayMode().getAmount(), PayChannelEnum.VOUCHER);
    }
}
