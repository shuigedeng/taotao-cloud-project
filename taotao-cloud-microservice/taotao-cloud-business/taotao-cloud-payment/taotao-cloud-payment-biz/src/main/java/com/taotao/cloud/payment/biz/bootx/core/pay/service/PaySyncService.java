package com.taotao.cloud.payment.biz.bootx.core.pay.service;

import cn.hutool.core.collection.CollUtil;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayStatusCode;
import com.taotao.cloud.payment.biz.bootx.code.pay.PaySyncStatus;
import com.taotao.cloud.payment.biz.bootx.core.pay.PayModelUtil;
import com.taotao.cloud.payment.biz.bootx.core.pay.builder.PaymentBuilder;
import com.taotao.cloud.payment.biz.bootx.core.pay.factory.PayStrategyFactory;
import com.taotao.cloud.payment.biz.bootx.core.pay.func.AbsPayStrategy;
import com.taotao.cloud.payment.biz.bootx.core.pay.func.PayStrategyConsumer;
import com.taotao.cloud.payment.biz.bootx.core.pay.result.PaySyncResult;
import com.taotao.cloud.payment.biz.bootx.core.payment.dao.PaymentManager;
import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import com.taotao.cloud.payment.biz.bootx.dto.pay.PayResult;
import com.taotao.cloud.payment.biz.bootx.dto.payment.PaymentDto;
import com.taotao.cloud.payment.biz.bootx.exception.payment.PayUnsupportedMethodException;
import com.taotao.cloud.payment.biz.bootx.mq.PaymentEventSender;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayModeParam;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 未完成的异步支付单与支付网关进行对比
 * @author xxm
 * @date 2021/4/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySyncService {

    private final PaymentManager paymentManager;
    private final PaymentEventSender eventSender;

    /**
     * 同步订单的支付状态
     */
    public PaymentDto syncByBusinessId(String businessId){
        List<Payment> payments = paymentManager.findByBusinessIdNoCancelDesc(businessId);
        if (CollUtil.isEmpty(payments)){
            return null;
        }
        Payment payment = payments.get(0);
        return this.syncPayment(payment).toDto();
    }

    /**
     * 同步支付状态
     */
    private Payment syncPayment(Payment payment){
        PayParam payParam = PaymentBuilder.buildPayParamByPayment(payment);
        // 1.获取支付方式，通过工厂生成对应的策略组
        List<AbsPayStrategy> paymentStrategyList = PayStrategyFactory.create(payParam.getPayModeList());
        if (CollUtil.isEmpty(paymentStrategyList)) {
            throw new PayUnsupportedMethodException();
        }

        // 2.初始化支付的参数
        for (AbsPayStrategy paymentStrategy : paymentStrategyList) {
            paymentStrategy.initPayParam(payment,payParam);
        }

        // 3 拿到异步支付方法, 与支付网关进行同步
        PayModeParam asyncPayMode = PayModelUtil.getAsyncPayModeParam(payParam);
        AbsPayStrategy syncPayStrategy = PayStrategyFactory.create(asyncPayMode);
        syncPayStrategy.initPayParam(payment,payParam);
        PaySyncResult paySyncResult = syncPayStrategy.doSyncPayStatusHandler();
        int paySyncStatus = paySyncResult.getPaySyncStatus();

        switch (paySyncStatus){
            // 支付成功
            case PaySyncStatus.TRADE_SUCCESS:{
                // payment 变更为支付成功
                this.paymentSuccess(payment,syncPayStrategy,paySyncResult);
                break;
            }
            // 待付款 理论上不会出现, 不进行处理
            case PaySyncStatus.WAIT_BUYER_PAY:{
                log.warn("依然是代付款状态");
                break;
            }
            // 网关已经超时关闭 和 网关没找到记录
            case PaySyncStatus.TRADE_CLOSED:
            case PaySyncStatus.NOT_FOUND: {
                // 判断下是否超时, 同时payment 变更为取消支付
                this.paymentCancel(payment,paymentStrategyList);
                break;
            }
            // 调用出错
            case PaySyncStatus.FAIL:{
                // 不进行处理
                log.warn("支付状态同步接口调用出错");
                break;
            }
            case PaySyncStatus.NOT_SYNC:
            default:{
                throw new BizException("代码有问题");
            }
        }
        return payment;
    }

    /**
     * payment 变更为取消支付
     */
    private void paymentCancel(Payment payment, List<AbsPayStrategy> absPayStrategies) {
        // 关闭本地支付记录
        this.doHandler(payment,absPayStrategies,(strategyList, paymentObj) -> {
            strategyList.forEach(AbsPayStrategy::doCloseHandler);
            // 修改payment支付状态为取消
            payment.setPayStatus(PayStatusCode.TRADE_CANCEL);
            paymentManager.save(payment);
        });
    }

    /**
     * payment 变更为已支付
     */
    private void paymentSuccess(Payment payment, AbsPayStrategy syncPayStrategy, PaySyncResult paySyncResult) {

        // 已支付不在重复处理
        if (Objects.equals(payment.getPayStatus(),PayStatusCode.TRADE_SUCCESS)){
            return;
        }
        syncPayStrategy.doAsyncSuccessHandler(paySyncResult.getMap());
        // 修改payment支付状态为成功
        payment.setPayStatus(PayStatusCode.TRADE_SUCCESS);
        payment.setPayTime(LocalDateTime.now());
        paymentManager.updateById(payment);

        // 发送成功事件
        PayResult payResult = PaymentBuilder.buildResultByPayment(payment);
        eventSender.sendPaymentCompleted(payResult);
    }

    /**
     * 处理方法
     * @param payment 支付记录
     * @param strategyList 支付策略
     * @param callback 回调
     */
    private void doHandler(Payment payment,
                           List<AbsPayStrategy> strategyList,
                           PayStrategyConsumer<List<AbsPayStrategy>, Payment> callback) {
        try {
            // 执行
            callback.accept(strategyList, payment);
        } catch (Exception e) {
            // error事件的处理
            this.errorHandler(payment, strategyList, e);
        }
    }
    /**
     * 对Error的处理
     */
    private void errorHandler(Payment payment, List<AbsPayStrategy> strategyList, Exception e) {
        // 待编写
        log.warn("支付状态同步方法报错了");
    }

}
