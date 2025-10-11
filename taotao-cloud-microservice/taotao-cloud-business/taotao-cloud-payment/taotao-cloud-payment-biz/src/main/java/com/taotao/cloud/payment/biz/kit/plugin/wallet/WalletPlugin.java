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

package com.taotao.cloud.payment.biz.kit.plugin.wallet;

import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.member.api.enums.DepositServiceTypeEnum;
import com.taotao.cloud.member.api.feign.MemberWalletApi;
import com.taotao.cloud.member.api.model.dto.MemberWalletUpdateDTO;
import com.taotao.cloud.payment.api.enums.CashierEnum;
import com.taotao.cloud.payment.api.enums.PaymentMethodEnum;
import com.taotao.cloud.payment.biz.entity.RefundLog;
import com.taotao.cloud.payment.biz.kit.CashierSupport;
import com.taotao.cloud.payment.biz.kit.Payment;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import com.taotao.cloud.payment.biz.kit.dto.PaymentSuccessParams;
import com.taotao.cloud.payment.biz.kit.params.dto.CashierParam;
import com.taotao.cloud.payment.biz.service.PaymentService;
import com.taotao.cloud.payment.biz.service.RefundLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.hibernate.service.spi.ServiceException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** WalletPlugin */
@Component
public class WalletPlugin implements Payment {

    /** 支付日志 */
    @Autowired
    private PaymentService paymentService;
    /** 退款日志 */
    @Autowired
    private RefundLogService refundLogService;
    /** 会员余额 */
    @Autowired
    private MemberWalletApi memberWalletApi;
    /** 收银台 */
    @Autowired
    private CashierSupport cashierSupport;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public Result<Object> h5pay(HttpServletRequest request, HttpServletResponse response, PayParam payParam) {
        savePaymentLog(payParam);
        return Result.success(ResultEnum.PAY_SUCCESS);
    }

    @Override
    public Result<Object> jsApiPay(HttpServletRequest request, PayParam payParam) {
        savePaymentLog(payParam);
        return Result.success(ResultEnum.PAY_SUCCESS);
    }

    @Override
    public Result<Object> appPay(HttpServletRequest request, PayParam payParam) {
        savePaymentLog(payParam);
        return Result.success(ResultEnum.PAY_SUCCESS);
    }

    @Override
    public Result<Object> nativePay(HttpServletRequest request, PayParam payParam) {
        if (payParam.getOrderType().equals(CashierEnum.RECHARGE.name())) {
            throw new BusinessException(ResultEnum.CAN_NOT_RECHARGE_WALLET);
        }
        savePaymentLog(payParam);
        return Result.success(ResultEnum.PAY_SUCCESS);
    }

    @Override
    public Result<Object> mpPay(HttpServletRequest request, PayParam payParam) {

        savePaymentLog(payParam);
        return Result.success(ResultEnum.PAY_SUCCESS);
    }

    @Override
    public void cancel(RefundLog refundLog) {

        try {
            memberWalletApi.increase(new MemberWalletUpdateDTO(
                    refundLog.getTotalAmount(),
                    refundLog.getMemberId(),
                    "取消[" + refundLog.getOrderSn() + "]订单，退还金额[" + refundLog.getTotalAmount() + "]",
                    DepositServiceTypeEnum.WALLET_REFUND.name()));
            refundLog.setIsRefund(true);
            refundLogService.save(refundLog);
        } catch (Exception e) {
            LogUtils.error("订单取消错误", e);
        }
    }

    /**
     * 保存支付日志
     *
     * @param payParam 支付参数
     */
    private void savePaymentLog(PayParam payParam) {
        // 同一个会员如果在不同的客户端使用预存款支付，会存在同时支付，无法保证预存款的正确性，所以对会员加锁
        RLock lock = redissonClient.getLock(UserContext.getCurrentUser().getId() + "");
        lock.lock();
        try {
            // 获取支付收银参数
            CashierParam cashierParam = cashierSupport.cashierParam(payParam);
            this.callBack(payParam, cashierParam);
        } catch (Exception e) {
            throw e;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void refund(RefundLog refundLog) {
        try {
            memberWalletApi.increase(new MemberWalletUpdateDTO(
                    refundLog.getTotalAmount(),
                    refundLog.getMemberId(),
                    "售后[" + refundLog.getAfterSaleNo() + "]审批，退还金额[" + refundLog.getTotalAmount() + "]",
                    DepositServiceTypeEnum.WALLET_REFUND.name()));
            refundLog.setIsRefund(true);
            refundLogService.save(refundLog);
        } catch (Exception e) {
            LogUtils.error("退款失败", e);
        }
    }

    /**
     * 支付订单
     *
     * @param payParam 支付参数
     * @param cashierParam 收银台参数
     */
    public void callBack(PayParam payParam, CashierParam cashierParam) {

        // 支付信息
        try {
            if (UserContext.getCurrentUser() == null) {
                throw new BusinessException(ResultEnum.USER_NOT_LOGIN);
            }
            boolean result = memberWalletApi.reduce(new MemberWalletUpdateDTO(
                    cashierParam.getPrice(),
                    UserContext.getCurrentUser().getId(),
                    "订单[" + cashierParam.getOrderSns() + "]支付金额[" + cashierParam.getPrice() + "]",
                    DepositServiceTypeEnum.WALLET_PAY.name()));

            if (result) {
                try {
                    PaymentSuccessParams paymentSuccessParams = new PaymentSuccessParams(
                            PaymentMethodEnum.WALLET.name(), "", cashierParam.getPrice(), payParam);

                    paymentService.success(paymentSuccessParams);
                    LogUtils.info("支付回调通知：余额支付：{}", payParam);
                } catch (ServiceException e) {
                    // 业务异常，则支付手动回滚
                    memberWalletApi.increase(new MemberWalletUpdateDTO(
                            cashierParam.getPrice(),
                            UserContext.getCurrentUser().getId(),
                            "订单[" + cashierParam.getOrderSns() + "]支付异常，余额返还[" + cashierParam.getPrice() + "]",
                            DepositServiceTypeEnum.WALLET_REFUND.name()));
                    throw e;
                }
            } else {
                throw new BusinessException(ResultEnum.WALLET_INSUFFICIENT);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            LogUtils.info("余额支付异常", e);
            throw new BusinessException(ResultEnum.WALLET_INSUFFICIENT);
        }
    }
}
