package com.taotao.cloud.payment.biz.daxpay.channel.wechat.strategy;

import com.taotao.cloud.payment.biz.daxpay.channel.wechat.service.assist.WechatAuthService;
import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.core.param.assist.AuthCodeParam;
import com.taotao.cloud.payment.biz.daxpay.core.param.assist.GenerateAuthUrlParam;
import com.taotao.cloud.payment.biz.daxpay.core.result.assist.AuthResult;
import com.taotao.cloud.payment.biz.daxpay.core.result.assist.AuthUrlResult;
import com.taotao.cloud.payment.biz.daxpay.service.strategy.AbsChannelAuthStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信认证
 * @author xxm
 * @since 2024/9/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatAuthStrategy extends AbsChannelAuthStrategy {
    private final WechatAuthService wechatAuthService;

    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    /**
     * 获取授权链接
     */
    @Override
    public AuthUrlResult generateAuthUrl(GenerateAuthUrlParam param) {
        return wechatAuthService.generateAuthUrl(param);
    }

    /**
     * 通过AuthCode兑换认证结果
     */
    @Override
    public AuthResult doAuth(AuthCodeParam param) {
        return wechatAuthService.getTokenAndOpenId(param.getAuthCode());
    }
}
