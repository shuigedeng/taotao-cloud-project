package com.taotao.cloud.payment.biz.daxpay.single.service.service.order.refund;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import com.taotao.cloud.payment.biz.daxpay.core.enums.RefundStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.core.exception.TradeNotExistException;
import com.taotao.cloud.payment.biz.daxpay.core.exception.TradeStatusErrorException;
import com.taotao.cloud.payment.biz.daxpay.core.param.trade.refund.RefundParam;
import com.taotao.cloud.payment.biz.daxpay.core.util.TradeNoGenerateUtil;
import com.taotao.cloud.payment.biz.daxpay.service.dao.order.pay.PayOrderManager;
import com.taotao.cloud.payment.biz.daxpay.service.dao.order.refund.RefundOrderManager;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.refund.RefundOrder;
import com.taotao.cloud.payment.biz.daxpay.service.param.order.refund.RefundCreateParam;
import com.taotao.cloud.payment.biz.daxpay.service.service.assist.PaymentAssistService;
import com.taotao.cloud.payment.biz.daxpay.service.service.trade.refund.RefundAssistService;
import com.taotao.cloud.payment.biz.daxpay.service.service.trade.refund.RefundService;
import com.taotao.cloud.payment.biz.daxpay.service.service.trade.refund.RefundSyncService;
import cn.hutool.extra.servlet.JakartaServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * 退款
 *
 * @author xxm
 * @since 2022/3/2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderService {

    private final PaymentAssistService paymentAssistService;

    private final PayOrderManager payOrderManager;

    private final RefundOrderManager refundOrderManager;

    private final RefundService refundService;

    private final RefundAssistService refundAssistService;

    private final RefundSyncService refundSyncService;

    /**
     * 创建退款订单
     */
    public void create(RefundCreateParam param) {

        var payOrder = payOrderManager.findByOrderNo(param.getOrderNo())
                .orElseThrow(() -> new TradeNotExistException("支付订单不存在"));

        // 初始化商户应用
        paymentAssistService.initMchApp(payOrder.getAppId());

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(JakartaServletUtil::getClientIP)
                .orElse("未知");

        // 构建退款参数并发起
        var refundParam = new RefundParam();
        refundParam.setAppId(payOrder.getAppId());
        refundParam.setClientIp(ip);
        refundParam.setReqTime(LocalDateTime.now());
        refundParam.setOrderNo(payOrder.getOrderNo());
        refundParam.setBizRefundNo("MANUAL_"+TradeNoGenerateUtil.refund());
        refundParam.setAmount(param.getAmount());
        refundService.refund(refundParam);
    }

    /**
     * 同步
     */
    public void sync(Long id) {
        RefundOrder refundOrder = refundOrderManager.findById(id)
                .orElseThrow(() -> new TradeNotExistException("退款订单不存在"));
        // 初始化商户应用
        paymentAssistService.initMchApp(refundOrder.getAppId());
        // 同步退款订单状态
        refundSyncService.syncRefundOrder(refundOrder);
    }

    /**
     * 退款重试
     */
    public void retry(Long id) {
        RefundOrder refundOrder = refundOrderManager.findById(id)
                .orElseThrow(() -> new TradeNotExistException("退款订单不存在"));
        // 初始化商户应用
        paymentAssistService.initMchApp(refundOrder.getAppId());

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(JakartaServletUtil::getClientIP)
                .orElse("未知");

        // 构建退款参数并发起
        var refundParam = new RefundParam();
        refundParam.setAppId(refundOrder.getAppId());
        refundParam.setClientIp(ip);
        refundParam.setReqTime(LocalDateTime.now());
        refundParam.setOrderNo(refundOrder.getOrderNo());
        refundParam.setBizRefundNo(refundOrder.getBizRefundNo());
        refundParam.setAmount(refundOrder.getAmount());
        // 发起退款
        refundService.refund(refundParam);
    }

    /**
     * 退款关闭
     */
    public void close(Long id) {
        RefundOrder refundOrder = refundOrderManager.findById(id)
                .orElseThrow(() -> new TradeNotExistException("退款订单不存在"));
        // 初始化商户应用
        paymentAssistService.initMchApp(refundOrder.getAppId());
        if (!Objects.equals(refundOrder.getStatus(), RefundStatusEnum.FAIL.getCode())) {
            throw new TradeStatusErrorException("只有失败状态的才可以关闭退款");
        }
        // 关闭
        refundAssistService.close(refundOrder);
    }
}
