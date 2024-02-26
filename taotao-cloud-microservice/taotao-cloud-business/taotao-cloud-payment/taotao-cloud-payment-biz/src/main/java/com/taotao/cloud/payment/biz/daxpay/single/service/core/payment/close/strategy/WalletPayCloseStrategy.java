package com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.close.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.param.channel.WalletPayParam;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wallet.entity.Wallet;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wallet.service.WalletPayService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wallet.service.WalletQueryService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wallet.service.WalletRecordService;
import com.taotao.cloud.payment.biz.daxpay.single.service.func.AbsPayCloseStrategy;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 钱包支付关闭策略
 * @author xxm
 * @since 2023/12/30
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WalletPayCloseStrategy extends AbsPayCloseStrategy {
    private final WalletPayService walletPayService;
    private final WalletQueryService walletQueryService;
    private final WalletRecordService walletRecordService;

    private Wallet wallet;


    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WALLET;
    }

    /**
     * 关闭前的处理方式
     */
    @Override
    public void doBeforeCloseHandler() {
        // 从通道扩展参数中取出钱包参数
        String channelExtra = this.getChannelOrder().getChannelExtra();
        WalletPayParam walletPayParam = JSONUtil.toBean(channelExtra, WalletPayParam.class);
        this.wallet = walletQueryService.getWallet(walletPayParam);
    }

    /**
     * 关闭操作
     */
    @Override
    public void doCloseHandler() {
        walletPayService.close(this.getChannelOrder(),this.wallet);
        walletRecordService.payClose(this.getChannelOrder(), this.getOrder().getTitle(), this.wallet);
    }
}
