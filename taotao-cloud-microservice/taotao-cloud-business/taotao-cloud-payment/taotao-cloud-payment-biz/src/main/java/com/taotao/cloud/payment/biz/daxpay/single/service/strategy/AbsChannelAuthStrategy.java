package com.taotao.cloud.payment.biz.daxpay.single.service.strategy;

import com.taotao.cloud.payment.biz.daxpay.core.param.assist.AuthCodeParam;
import com.taotao.cloud.payment.biz.daxpay.core.param.assist.GenerateAuthUrlParam;
import com.taotao.cloud.payment.biz.daxpay.core.result.assist.AuthResult;
import com.taotao.cloud.payment.biz.daxpay.core.result.assist.AuthUrlResult;

/**
 * 通道抽象认证策略,
 * @author xxm
 * @since 2024/9/24
 */
public abstract class AbsChannelAuthStrategy implements PaymentStrategy {

    /**
     * 获取授权链接
     */
    public abstract AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param);

    /**
     * 通过AuthCode获取认证结果
     */
    public abstract AuthResult doAuth(AuthCodeParam param);


}
