package com.taotao.cloud.payment.biz.daxpay.single.service.convert.config;

import com.taotao.cloud.payment.biz.daxpay.service.entity.config.ChannelConfig;
import com.taotao.cloud.payment.biz.daxpay.service.result.config.ChannelConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 通道配置
 * @author xxm
 * @since 2024/6/25
 */
@Mapper
public interface ChannelConfigConvert {
    ChannelConfigConvert INSTANCE = Mappers.getMapper(ChannelConfigConvert.class);

    ChannelConfigResult toResult(ChannelConfig in);
}
