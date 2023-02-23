package com.taotao.cloud.payment.biz.bootx.core.pay.convert;

import com.taotao.cloud.payment.biz.bootx.dto.payment.PayChannelInfo;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayModeParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
* 支付对象转换
* @author xxm
* @date 2022/3/2
*/
@Mapper
public interface PayConvert {
    PayConvert CONVERT = Mappers.getMapper(PayConvert.class);

    PayChannelInfo convert(PayModeParam in);

}
