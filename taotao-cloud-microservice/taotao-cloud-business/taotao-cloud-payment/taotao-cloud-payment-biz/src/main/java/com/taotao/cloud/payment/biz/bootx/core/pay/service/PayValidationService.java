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

package com.taotao.cloud.payment.biz.bootx.core.pay.service;

import com.taotao.cloud.payment.biz.bootx.code.pay.PayChannelCode;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayAmountAbnormalException;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayFailureException;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayModeParam;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayParam;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付时校验服务
 *
 * @author xxm
 * @date 2021/7/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayValidationService {

    /** 检查支付金额 */
    public void validationAmount(List<PayModeParam> payModeList) {
        for (PayModeParam payModeParam : payModeList) {
            // 同时满足支付金额小于等于零
            if (BigDecimalUtil.compareTo(payModeParam.getAmount(), BigDecimal.ZERO) < 1) {
                throw new PayAmountAbnormalException();
            }
        }
    }

    /** 检查异步支付方式 */
    public void validationAsyncPayMode(PayParam payParam) {
        // 组合支付时只允许有一个异步支付方式
        List<PayModeParam> payModeList = payParam.getPayModeList();

        long asyncPayModeCount =
                payModeList.stream()
                        .map(PayModeParam::getPayChannel)
                        .filter(PayChannelCode.ASYNC_TYPE::contains)
                        .count();
        if (asyncPayModeCount > 1) {
            throw new PayFailureException("组合支付时只允许有一个异步支付方式");
        }
    }
}
