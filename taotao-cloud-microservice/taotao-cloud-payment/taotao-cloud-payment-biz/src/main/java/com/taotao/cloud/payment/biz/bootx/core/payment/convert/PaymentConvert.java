package com.taotao.cloud.payment.biz.bootx.core.payment.convert;

import com.taotao.cloud.payment.biz.bootx.core.payment.entity.Payment;
import com.taotao.cloud.payment.biz.bootx.dto.payment.PaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
* 支付记录转换
* @author xxm
* @date 2021/8/20
*/
@Mapper
public interface PaymentConvert {
    PaymentConvert CONVERT = Mappers.getMapper(PaymentConvert.class);

    PaymentDto convert(Payment payment);
}
