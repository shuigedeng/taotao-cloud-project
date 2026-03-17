package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.service.entity.config;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.core.annotation.BigField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.payment.biz.daxpay.core.enums.ChannelEnum;
import com.taotao.cloud.payment.biz.daxpay.service.common.entity.MchAppBaseEntity;
import com.taotao.cloud.payment.biz.daxpay.service.convert.config.ChannelConfigConvert;
import com.taotao.cloud.payment.biz.daxpay.service.result.config.ChannelConfigResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通道支付配置
 * @author xxm
 * @since 2024/5/27
 */
@EqualsAndHashCode(callSuper = true)
@Data

@TableName("pay_channel_config" )
public class ChannelConfig extends MchAppBaseEntity implements ToResult<ChannelConfigResult> {

    /**
     * 支付通道
     * @see ChannelEnum
     */
    private String channel;

    /** 通道商户号 */
    private String outMchNo;

    /** 通道APPID */
    private String outAppId;

    /** 是否启用 */
    private boolean enable;

    /** 扩展存储 */
    @BigField
    private String ext;


    @Override
    public ChannelConfigResult toResult() {
        return ChannelConfigConvert.INSTANCE.toResult(this);
    }
}
