package com.taotao.cloud.payment.biz.daxpay.single.service.service.develop;

import com.taotao.cloud.payment.biz.daxpay.core.param.PaymentCommonParam;
import com.taotao.cloud.payment.biz.daxpay.core.param.trade.pay.PayParam;
import com.taotao.cloud.payment.biz.daxpay.core.param.trade.refund.RefundParam;
import com.taotao.cloud.payment.biz.daxpay.core.param.trade.transfer.TransferParam;
import com.taotao.cloud.payment.biz.daxpay.core.result.trade.pay.PayResult;
import com.taotao.cloud.payment.biz.daxpay.core.result.trade.refund.RefundResult;
import com.taotao.cloud.payment.biz.daxpay.core.result.trade.transfer.TransferResult;
import com.taotao.cloud.payment.biz.daxpay.service.service.assist.PaymentAssistService;
import com.taotao.cloud.payment.biz.daxpay.service.service.trade.pay.PayService;
import com.taotao.cloud.payment.biz.daxpay.service.service.trade.refund.RefundService;
import com.taotao.cloud.payment.biz.daxpay.service.service.trade.transfer.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 交易开发调试服务商
 * @author xxm
 * @since 2024/9/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DevelopTradeService {
    private final PaymentAssistService paymentAssistService;
    private final PayService payService;
    private final RefundService refundService;
    private final TransferService transferService;
    /**
     * 生成签名
     */
    public String genSign(PaymentCommonParam param){
        paymentAssistService.initMchApp(param.getAppId());
        return paymentAssistService.genSign(param);
    }

    /**
     * 支付
     */
    public PayResult pay(PayParam param) {
        // 初始化
        paymentAssistService.initMchApp(param.getAppId());
        // 签名校验
        paymentAssistService.signVerify(param);
        return payService.pay(param);
    }

    /**
     * 退款
     */
    public RefundResult refund(RefundParam param) {
        // 初始化
        paymentAssistService.initMchApp(param.getAppId());
        // 签名校验
        paymentAssistService.signVerify(param);
        return refundService.refund(param);
    }

    /**
     * 转账
     */
    public TransferResult transfer(TransferParam param) {
        // 初始化
        paymentAssistService.initMchApp(param.getAppId());
        // 签名校验
        paymentAssistService.signVerify(param);
        return transferService.transfer(param);
    }
}
