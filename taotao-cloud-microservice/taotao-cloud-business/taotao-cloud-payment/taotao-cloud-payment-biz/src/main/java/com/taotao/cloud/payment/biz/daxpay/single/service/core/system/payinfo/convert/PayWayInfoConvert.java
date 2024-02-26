package com.taotao.cloud.payment.biz.daxpay.single.service.core.system.payinfo.convert;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.system.payinfo.entity.PayWayInfo;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.system.payinfo.PayWayInfoDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.system.payinfo.PayWayInfoParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/8
 */
@Mapper
public interface PayWayInfoConvert {
    PayWayInfoConvert CONVERT = Mappers.getMapper(PayWayInfoConvert.class);

    PayWayInfo convert(PayWayInfoParam in);

    PayWayInfoDto convert(PayWayInfo in);
}
