package com.taotao.cloud.payment.biz.daxpay.single.service.strategy;

import com.taotao.cloud.payment.biz.daxpay.core.param.cashier.CashierAuthCodeParam;
import com.taotao.cloud.payment.biz.daxpay.core.param.cashier.CashierAuthUrlParam;
import com.taotao.cloud.payment.biz.daxpay.core.param.cashier.CashierPayParam;
import com.taotao.cloud.payment.biz.daxpay.core.param.trade.pay.PayParam;
import com.taotao.cloud.payment.biz.daxpay.core.result.assist.AuthResult;

/**
 * 抽象通道收银台策略
 * @author xxm
 * @since 2024/9/28
 */
public abstract class AbsChannelCashierStrategy implements PaymentStrategy{

    /**
     * 生成授权链接, 主要是微信类通道使用, 用于获取OpenId
     */
    public String generateAuthUrl(CashierAuthUrlParam param) {
        return "";
    }

    /**
     * 获取认证结果
     */
    public AuthResult doAuth(CashierAuthCodeParam param) {
        return new AuthResult();
    }

    /**
     * 支付参数处理
     */
    public void handlePayParam(CashierPayParam cashierPayParam, PayParam payParam) {
    }

}
