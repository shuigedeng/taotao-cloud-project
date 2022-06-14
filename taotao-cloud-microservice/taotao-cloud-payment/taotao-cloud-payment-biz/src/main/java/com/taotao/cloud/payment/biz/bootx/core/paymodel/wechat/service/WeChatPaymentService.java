package com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.service;

import cn.bootx.common.core.exception.BizException;
import cn.bootx.common.core.util.BigDecimalUtil;
import cn.bootx.payment.code.pay.PayChannelCode;
import cn.bootx.payment.code.pay.PayStatusCode;
import cn.bootx.payment.core.payment.dao.PaymentManager;
import cn.bootx.payment.core.payment.entity.Payment;
import cn.bootx.payment.core.paymodel.wechat.dao.WeChatPaymentManager;
import cn.bootx.payment.core.paymodel.wechat.entity.WeChatPayment;
import cn.bootx.payment.dto.payment.PayChannelInfo;
import cn.bootx.payment.dto.payment.RefundableInfo;
import cn.bootx.payment.param.pay.PayModeParam;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 微信支付记录单
 * @author xxm
 * @date 2021/6/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPaymentService {
    private final PaymentManager paymentManager;
    private final WeChatPaymentManager weChatPaymentManager;

    /**
     * 支付调起成功
     * 更新 payment 中 异步支付类型信息
     */
    public void updatePaySuccess(Payment payment, PayModeParam payModeParam){
        payment.setAsyncPayMode(true)
                .setAsyncPayChannel(PayChannelCode.WECHAT);

        List<PayChannelInfo> payTypeInfos = payment.getPayChannelInfoList();
        List<RefundableInfo> refundableInfos = payment.getRefundableInfoList();
        // 清除已有的异步支付类型信息
        payTypeInfos.removeIf(payTypeInfo -> PayChannelCode.ASYNC_TYPE.contains(payTypeInfo.getPayChannel()));
        refundableInfos.removeIf(payTypeInfo -> PayChannelCode.ASYNC_TYPE.contains(payTypeInfo.getPayChannel()));
        // 添加微信支付类型信息
        payTypeInfos.add(new PayChannelInfo()
                .setPayChannel(PayChannelCode.WECHAT)
                .setPayWay(payModeParam.getPayWay())
                .setAmount(payModeParam.getAmount())
                .setExtraParamsJson(payModeParam.getExtraParamsJson()));
        payment.setPayChannelInfo(JSONUtil.toJsonStr(payTypeInfos));
        // 更新微信可退款类型信息
        refundableInfos.add(new RefundableInfo()
                .setPayChannel(PayChannelCode.WECHAT)
                .setAmount(payModeParam.getAmount()));
        payment.setRefundableInfo(JSONUtil.toJsonStr(payTypeInfos))
                .setRefundableInfo(JSONUtil.toJsonStr(refundableInfos));
        paymentManager.updateById(payment);
    }

    /**
     * 更新支付记录成功状态, 并创建微信支付记录
     */
    public void updateSyncSuccess(Long id, PayModeParam payModeParam, String tradeNo) {
        // 更新支付记录
        Payment payment = paymentManager.findById(id)
                .orElseThrow(() -> new BizException("支付记录不存在"));

        // 创建支付宝支付记录
        WeChatPayment wechatPayment = new WeChatPayment();
        wechatPayment.setTradeNo(tradeNo)
                .setPaymentId(payment.getId())
                .setAmount(payModeParam.getAmount())
                .setBusinessId(payment.getBusinessId())
                .setUserId(payment.getUserId())
                .setPayStatus(PayStatusCode.TRADE_SUCCESS)
                .setPayTime(LocalDateTime.now());
        weChatPaymentManager.updateById(wechatPayment);
    }

    /**
     * 取消状态
     */
    public void updateClose(Long paymentId){
        Optional<WeChatPayment> weChatPaymentOptional = weChatPaymentManager.findByPaymentId(paymentId);
        weChatPaymentOptional.ifPresent(weChatPayment -> {
            weChatPayment.setPayStatus(PayStatusCode.TRADE_CANCEL);
            weChatPaymentManager.updateById(weChatPayment);
        });
    }


    /**
     * 更新退款
     */
    public void updatePayRefund(Long paymentId, BigDecimal amount) {
        Optional<WeChatPayment> weChatPayment = weChatPaymentManager.findByPaymentId(paymentId);
        weChatPayment.ifPresent(payment -> {
            BigDecimal refundableBalance = payment.getRefundableBalance().subtract(amount);
            payment.setRefundableBalance(refundableBalance);
            if (BigDecimalUtil.compareTo(refundableBalance, BigDecimal.ZERO)==0){
                payment.setPayStatus(PayStatusCode.TRADE_REFUNDED);
            } else {
                payment.setPayStatus(PayStatusCode.TRADE_REFUNDING);
            }
            weChatPaymentManager.updateById(payment);
        });
    }
}
