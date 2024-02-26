package com.taotao.cloud.payment.biz.daxpay.single.service.core.system.config.convert;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.system.config.entity.PayChannelConfig;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.system.config.PayChannelConfigDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.system.payinfo.PayChannelInfoParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/8
 */
@Mapper
public interface PayChannelConfigConvert {
    PayChannelConfigConvert CONVERT = Mappers.getMapper(PayChannelConfigConvert.class);

    PayChannelConfig convert(PayChannelInfoParam in);

    PayChannelConfigDto convert(PayChannelConfig in);
}
