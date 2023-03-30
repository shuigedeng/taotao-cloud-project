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
import com.taotao.cloud.payment.biz.bootx.code.pay.PayChannelCode;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayStatusCode;
import com.taotao.cloud.payment.biz.bootx.core.pay.PayModelUtil;
import com.taotao.cloud.payment.biz.bootx.core.pay.builder.PaymentBuilder;
import com.taotao.cloud.payment.biz.bootx.core.pay.factory.PayStrategyFactory;
import com.taotao.cloud.payment.biz.bootx.core.pay.func.AbsPayStrategy;
import com.taotao.cloud.payment.biz.bootx.core.pay.func.PayStrategyConsumer;
import com.taotao.cloud.payment.biz.bootx.core.payment.dao.PaymentManager;
import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import com.taotao.cloud.payment.biz.bootx.core.payment.service.PaymentService;
import com.taotao.cloud.payment.biz.bootx.dto.pay.PayResult;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayFailureException;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayNotExistedException;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayUnsupportedMethodException;
import com.taotao.cloud.payment.biz.bootx.mq.PaymentEventSender;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayModeParam;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayParam;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 支付流程
 *
 * @author xxm
 * @date 2020/12/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayService {
    private final PaymentService paymentService;
    private final PayValidationService payValidationService;
    private final PaymentManager paymentManager;

    private final PaymentEventSender eventSender;

    /**
     * 支付方法(同步/异步/组合支付) 同步支付：都只会在第一次执行中就完成支付，例如钱包、积分都是调用完就进行了扣减，完成了支付记录
     * 异步支付：例如支付宝、微信，发起支付后还需要跳转第三方平台进行支付，支付后进行回调支付中心，才完成支付记录
     * 组合支付：主要是混合了同步支付和异步支付，同时异步支付只能有一个，在支付时先对同步支付进行扣减，然后异步支付回调完成完成整个支付单
     * 组合支付在非第一次支付的时候，只对新传入的异步支付PayMode进行处理，PayMode的价格使用第一次发起的价格，旧的同步支付如果传入后也不做处理，
     * Payment中PayModeList将会为 旧有的同步支付+新传入的异步支付方式(在具体支付实现中处理)
     */
    @Transactional(rollbackFor = Exception.class)
    public PayResult pay(PayParam payParam) {
        // 支付参数检查
        payValidationService.validationAsyncPayMode(payParam);

        // 获取并校验支付状态
        Payment payment = paymentService.getAndCheckPaymentByBusinessId(payParam.getBusinessId());

        // 异步支付且非第一次支付
        if (Objects.nonNull(payment) && payment.isAsyncPayMode()) {
            return this.paySyncNotFirst(payParam, payment);
        } else {
            // 首次支付或同步支付
            return this.payFirst(payParam, payment);
        }
    }

    /** 发起的第一次支付请求(同步/异步) */
    private PayResult payFirst(PayParam payParam, Payment payment) {
        // 0. 支付成功直接返回
        if (Objects.nonNull(payment)) {
            return PaymentBuilder.buildResultByPayment(payment);
        }

        // 1. 价格检测
        payValidationService.validationAmount(payParam.getPayModeList());

        // 2. 创建支付记录
        payment = this.createPayment(payParam);

        // 3. 调用支付方法进行发起支付
        this.payMethod(payParam, payment);

        // 4. 获取支付记录信息
        payment = paymentManager.findById(payment.getId()).orElseThrow(PayNotExistedException::new);

        // 5. 返回支付结果
        PayResult payResult = PaymentBuilder.buildResultByPayment(payment);

        // 如果是支付成功, 发送事件
        if (Objects.equals(payResult.getPayStatus(), PayStatusCode.TRADE_SUCCESS)) {
            eventSender.sendPaymentCompleted(payResult);
        }
        return payResult;
    }

    /** 异步支付执行(非第一次请求), 只执行异步支付策略, 报错不影响继续发起支付 */
    private PayResult paySyncNotFirst(PayParam payParam, Payment payment) {

        // 0. 处理支付完成情况(完成/退款)
        List<Integer> trades =
                Arrays.asList(
                        PayStatusCode.TRADE_SUCCESS,
                        PayStatusCode.TRADE_REFUNDING,
                        PayStatusCode.TRADE_REFUNDED);
        if (trades.contains(payment.getPayStatus())) {
            return PaymentBuilder.buildResultByPayment(payment);
        }

        // 1.获取 异步支付 通道，通过工厂生成对应的策略组
        PayParam oldPayParam = PaymentBuilder.buildPayParamByPayment(payment);
        PayModeParam payModeParam = this.getAsyncPayModeParam(payParam, oldPayParam);
        List<AbsPayStrategy> paymentStrategyList =
                PayStrategyFactory.create(Collections.singletonList(payModeParam));

        // 2.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payment, payParam);
        }

        // 3.支付前准备
        this.doHandler(payment, paymentStrategyList, AbsPayStrategy::doBeforePay, null);

        // 4. 发起支付
        this.doHandler(
                payment,
                paymentStrategyList,
                AbsPayStrategy::doPayHandler,
                (strategyList, paymentObj) -> {
                    // 发起支付成功进行的执行方法
                    strategyList.forEach(AbsPayStrategy::doSuccessHandler);
                });

        // 5. 获取支付记录信息
        payment = paymentManager.findById(payment.getId()).orElseThrow(PayNotExistedException::new);

        // 6. 组装返回参数
        return PaymentBuilder.buildResultByPayment(payment);
    }

    /** 执行支付方法 */
    private void payMethod(PayParam payParam, Payment payment) {

        // 1.获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> paymentStrategyList =
                PayStrategyFactory.create(payParam.getPayModeList());
        if (CollectionUtil.isEmpty(paymentStrategyList)) {
            throw new PayUnsupportedMethodException();
        }

        // 2.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payment, payParam);
        }

        // 3.支付前准备
        this.doHandler(payment, paymentStrategyList, AbsPayStrategy::doBeforePay, null);

        // 4.支付
        this.doHandler(
                payment,
                paymentStrategyList,
                AbsPayStrategy::doPayHandler,
                (strategyList, paymentObj) -> {
                    // 发起支付成功进行的执行方法
                    strategyList.forEach(AbsPayStrategy::doSuccessHandler);
                    // 所有支付方式都是同步时进行Payment处理
                    if (PayModelUtil.isNotSync(payParam.getPayModeList())) {
                        // 修改payment支付状态为成功
                        paymentObj.setPayStatus(PayStatusCode.TRADE_SUCCESS);
                        paymentObj.setPayTime(LocalDateTime.now());
                    }
                    paymentManager.updateById(paymentObj);
                });
    }

    /**
     * 执行策略中不同的handler
     *
     * @param payment 主支付对象
     * @param strategyList 策略列表
     * @param payMethod 执行支付/支付前的函数
     * @param successMethod 执行成功的函数
     */
    private void doHandler(
            Payment payment,
            List<AbsPayStrategy> strategyList,
            Consumer<AbsPayStrategy> payMethod,
            PayStrategyConsumer<List<AbsPayStrategy>, Payment> successMethod) {
        // 执行策略操作，如支付前/支付时
        // 等同strategyList.forEach(payMethod.accept(PaymentStrategy))
        strategyList.forEach(payMethod);

        // 执行操作成功的处理
        Optional.ofNullable(successMethod)
                .ifPresent(function -> function.accept(strategyList, payment));
    }

    /** 获取异步支付参数 */
    private PayModeParam getAsyncPayModeParam(PayParam payParam, PayParam oldPaymentParam) {

        List<PayModeParam> oldPayModes = oldPaymentParam.getPayModeList();
        // 旧的异步支付方式
        PayModeParam oldModeParam =
                oldPayModes.stream()
                        .filter(
                                payMode ->
                                        PayChannelCode.ASYNC_TYPE.contains(payMode.getPayChannel()))
                        .findFirst()
                        .orElseThrow(() -> new PayFailureException("支付方式数据异常"));

        // 新的异步支付方式
        PayModeParam payModeParam =
                payParam.getPayModeList().stream()
                        .filter(
                                payMode ->
                                        PayChannelCode.ASYNC_TYPE.contains(payMode.getPayChannel()))
                        .findFirst()
                        .orElseThrow(() -> new PayFailureException("支付方式数据异常"));
        payModeParam.setAmount(oldModeParam.getAmount());

        return payModeParam;
    }

    /** 创建支付记录 */
    private Payment createPayment(PayParam payParam) {
        // 构建payment记录 并保存
        Payment payment = PaymentBuilder.buildPayment(payParam);
        return paymentManager.save(payment);
    }
}
