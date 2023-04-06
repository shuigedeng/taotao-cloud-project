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

package com.taotao.cloud.payment.biz.service.impl;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.payment.biz.kit.CashierSupport;
import com.taotao.cloud.payment.biz.kit.dto.PaymentSuccessParams;
import com.taotao.cloud.payment.biz.kit.params.CashierExecute;
import com.taotao.cloud.payment.biz.service.PaymentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 支付日志 业务实现 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private List<CashierExecute> cashierExecutes;

    @Autowired
    private CashierSupport cashierSupport;

    @Override
    public void success(PaymentSuccessParams paymentSuccessParams) {

        boolean paymentResult = cashierSupport.paymentResult(paymentSuccessParams.getPayParam());
        if (paymentResult) {
            LogUtils.info("订单支付状态后，调用支付成功接口，流水号：{}", paymentSuccessParams.getReceivableNo());
            return;
        }

        LogUtils.info("支付成功，第三方流水：{}", paymentSuccessParams.getReceivableNo());
        // 支付结果处理
        for (CashierExecute cashierExecute : cashierExecutes) {
            cashierExecute.paymentSuccess(paymentSuccessParams);
        }
    }

    @Override
    public void adminPaySuccess(PaymentSuccessParams paymentSuccessParams) {
        LogUtils.info("支付状态修改成功->银行转账");
        // 支付结果处理
        for (CashierExecute cashierExecute : cashierExecutes) {
            cashierExecute.paymentSuccess(paymentSuccessParams);
        }
    }
}
