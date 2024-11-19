package com.taotao.cloud.payment.biz.daxpay.channel.alipay.convert.config;

import com.taotao.cloud.payment.biz.daxpay.channel.alipay.entity.config.AliPayConfig;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.param.config.AliPayConfigParam;
import com.taotao.cloud.payment.biz.daxpay.channel.alipay.result.config.AlipayConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/6/25
 */
@Mapper
public interface AlipayConfigConvert {
    AlipayConfigConvert CONVERT = Mappers.getMapper(AlipayConfigConvert.class);

    AlipayConfigResult toResult(AliPayConfig in);

    AliPayConfig copy(AliPayConfig in);

    AliPayConfig toEntity(AliPayConfigParam in);
}
