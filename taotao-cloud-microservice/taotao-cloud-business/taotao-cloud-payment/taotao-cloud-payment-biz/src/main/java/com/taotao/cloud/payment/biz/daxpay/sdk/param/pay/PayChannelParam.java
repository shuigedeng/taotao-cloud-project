package com.taotao.cloud.payment.biz.daxpay.sdk.param.pay;

import com.taotao.cloud.payment.biz.daxpay.sdk.code.PayChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.sdk.code.PayWayEnum;
import com.taotao.cloud.payment.biz.daxpay.sdk.param.ChannelParam;
import com.taotao.cloud.payment.biz.daxpay.sdk.param.channel.AliPayParam;
import com.taotao.cloud.payment.biz.daxpay.sdk.param.channel.VoucherPayParam;
import com.taotao.cloud.payment.biz.daxpay.sdk.param.channel.WalletPayParam;
import com.taotao.cloud.payment.biz.daxpay.sdk.param.channel.WeChatPayParam;
import lombok.Getter;
import lombok.Setter;

/**
 * 同意下单支付方式参数
 *
 * @author xxm
 * @since 2020/12/8
 */
@Getter
@Setter
public class PayChannelParam {

    /**
     * 支付通道编码
     * @see PayChannelEnum#getCode()
     */
    private String channel;

    /**
     * 支付方式编码
     * @see PayWayEnum#getCode()
     */
    private String way;

    /** 支付金额 */
    private Integer amount;

    /**
     * 附加支付参数, 传输json格式字符串
     * @see AliPayParam
     * @see WeChatPayParam
     * @see VoucherPayParam
     * @see WalletPayParam
     */
    private ChannelParam channelParam;
}
