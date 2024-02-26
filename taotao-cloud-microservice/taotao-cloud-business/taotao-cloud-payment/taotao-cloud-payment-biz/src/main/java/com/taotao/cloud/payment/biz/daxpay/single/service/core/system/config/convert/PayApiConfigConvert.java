package com.taotao.cloud.payment.biz.daxpay.single.service.core.system.config.convert;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.system.config.entity.PayApiConfig;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.system.config.PayApiConfigDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.system.config.PayApiConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 开放接口信息转换
 * @author xxm
 * @since 2023/12/22
 */
@Mapper
public interface PayApiConfigConvert {
    PayApiConfigConvert CONVERT = Mappers.getMapper(PayApiConfigConvert.class);

    PayApiConfig convert(PayApiConfigParam in);

    PayApiConfigDto convert(PayApiConfig in);
}
