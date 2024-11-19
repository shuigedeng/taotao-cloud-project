package com.taotao.cloud.payment.biz.daxpay.single.service.convert.config;

import com.taotao.cloud.payment.biz.daxpay.service.entity.config.PlatformConfig;
import com.taotao.cloud.payment.biz.daxpay.service.result.config.PlatformConfigResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/9/19
 */
@Mapper
public interface PlatformConfigConvert {
    PlatformConfigConvert CONVERT = Mappers.getMapper(PlatformConfigConvert.class);

    PlatformConfigResult toResult(PlatformConfig entity);
}
