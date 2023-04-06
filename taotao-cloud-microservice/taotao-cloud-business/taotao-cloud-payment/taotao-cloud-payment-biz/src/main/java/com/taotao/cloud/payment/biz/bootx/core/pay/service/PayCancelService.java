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

import cn.hutool.core.collection.CollectionUtil;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayStatusCode;
import com.taotao.cloud.payment.biz.bootx.core.pay.builder.PaymentBuilder;
import com.taotao.cloud.payment.biz.bootx.core.pay.factory.PayStrategyFactory;
import com.taotao.cloud.payment.biz.bootx.core.pay.func.AbsPayStrategy;
import com.taotao.cloud.payment.biz.bootx.core.pay.func.PayStrategyConsumer;
import com.taotao.cloud.payment.biz.bootx.core.payment.dao.PaymentManager;
import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayFailureException;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayUnsupportedMethodException;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayParam;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 取消订单处理
 *
 * @author xxm
 * @date 2021/3/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCancelService {
    private final PaymentManager paymentManager;
    private final PaymentService paymentService;

    /** 根据业务id取消支付记录 */
    @Transactional(rollbackFor = Exception.class)
    public void cancelByBusinessId(String businessId) {
        Optional<Payment> paymentOptional =
                Optional.ofNullable(paymentService.getAndCheckPaymentByBusinessId(businessId));
        paymentOptional.ifPresent(this::cancelPayment);
    }

    /** 根据paymentId取消支付记录 */
    @Transactional(rollbackFor = Exception.class)
    public void cancelByPaymentId(Long paymentId) {
        // 获取payment和paymentParam数据
        Payment payment = paymentManager.findById(paymentId).orElseThrow(() -> new PayFailureException("未找到payment"));
        this.cancelPayment(payment);
    }

    /** 取消支付记录 */
    private void cancelPayment(Payment payment) {

        // 获取 paymentParam
        PayParam payParam = PaymentBuilder.buildPayParamByPayment(payment);
        ;

        // 1.获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> paymentStrategyList = PayStrategyFactory.create(payParam.getPayModeList());
        if (CollectionUtil.isEmpty(paymentStrategyList)) {
            throw new PayUnsupportedMethodException();
        }

        // 2.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payment, payParam);
        }

        // 3.执行取消订单
        this.doHandler(payment, paymentStrategyList, (strategyList, paymentObj) -> {
            // 发起取消进行的执行方法
            strategyList.forEach(AbsPayStrategy::doCancelHandler);
            // 取消订单
            paymentObj.setPayStatus(PayStatusCode.TRADE_CANCEL);
            paymentManager.updateById(paymentObj);
        });
    }

    /**
     * 处理方法
     *
     * @param payment 支付记录
     * @param strategyList 支付策略
     * @param successCallback 成功操作
     */
    private void doHandler(
            Payment payment,
            List<AbsPayStrategy> strategyList,
            PayStrategyConsumer<List<AbsPayStrategy>, Payment> successCallback) {

        try {
            // 执行
            successCallback.accept(strategyList, payment);
        } catch (Exception e) {
            // error事件的处理
            this.errorHandler(payment, strategyList, e);
            throw e;
        }
    }

    /** 对Error的处理 */
    private void errorHandler(Payment payment, List<AbsPayStrategy> strategyList, Exception e) {
        // 待编写
        log.warn("取消订单失败");
    }
}
