package com.taotao.cloud.payment.biz.daxpay.single.service.service.order.pay;

import com.taotao.cloud.payment.biz.daxpay.core.exception.TradeNotExistException;
import com.taotao.cloud.payment.biz.daxpay.service.dao.order.pay.PayOrderManager;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.pay.PayOrder;
import com.taotao.cloud.payment.biz.daxpay.service.service.assist.PaymentAssistService;
import com.taotao.cloud.payment.biz.daxpay.service.service.trade.pay.PayCloseService;
import com.taotao.cloud.payment.biz.daxpay.service.service.trade.pay.PaySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付订单服务
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderService {
    private final PayOrderManager payOrderManager;
    private final PaySyncService paySyncService;

    private final PaymentAssistService paymentAssistService;
    private final PayCloseService payCloseService;

    /**
     * 同步
     */
    public void sync(Long id) {
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        // 初始化商户应用
        paymentAssistService.initMchApp(payOrder.getAppId());
        paySyncService.syncPayOrder(payOrder);
    }

    /**
     * 关闭订单
     */
    public void close(Long id) {
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        // 初始化商户应用
        paymentAssistService.initMchApp(payOrder.getAppId());
        payCloseService.closeOrder(payOrder,false);
    }

    /**
     * 撤销订单
     */
    public void cancel(Long id) {
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        // 初始化商户应用
        paymentAssistService.initMchApp(payOrder.getAppId());
        payCloseService.closeOrder(payOrder,true);
    }

}
