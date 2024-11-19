package com.taotao.cloud.payment.biz.daxpay.channel.wechat.convert.config;

import com.taotao.cloud.payment.biz.daxpay.channel.wechat.entity.config.WechatPayConfig;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.param.config.WechatPayConfigParam;
import com.taotao.cloud.payment.biz.daxpay.channel.wechat.result.config.WechatPayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 微信支付配置
 * @author xxm
 * @since 2024/7/17
 */
@Mapper
public interface WechatPayConfigConvert {
    WechatPayConfigConvert CONVERT = Mappers.getMapper(WechatPayConfigConvert.class);

    WechatPayConfigResult toResult(WechatPayConfig in);

    WechatPayConfig copy(WechatPayConfig in);

    WechatPayConfig toEntity(WechatPayConfigParam in);
}
