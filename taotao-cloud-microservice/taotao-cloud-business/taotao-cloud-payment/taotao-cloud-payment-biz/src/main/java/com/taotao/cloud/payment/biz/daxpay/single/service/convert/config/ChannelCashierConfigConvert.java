package com.taotao.cloud.payment.biz.daxpay.single.service.convert.config;

import com.taotao.cloud.payment.biz.daxpay.service.entity.config.ChannelCashierConfig;
import com.taotao.cloud.payment.biz.daxpay.service.param.config.ChannelCashierConfigParam;
import com.taotao.cloud.payment.biz.daxpay.service.result.config.ChannelCashierConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 通道收银台配置
 * @author xxm
 * @since 2024/9/28
 */
@Mapper
public interface ChannelCashierConfigConvert {
    ChannelCashierConfigConvert CONVERT = Mappers.getMapper(ChannelCashierConfigConvert.class);

    ChannelCashierConfig toEntity(ChannelCashierConfigParam param);

    ChannelCashierConfigResult toResult(ChannelCashierConfig channelCashierConfig);
}
