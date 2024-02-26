package com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.alipay.convert;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.alipay.entity.AliPayRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.alipay.AliPayConfigDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.alipay.AliPayRecordDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.channel.alipay.AliPayConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付宝转换
 *
 * @author xxm
 * @since 2021/7/5
 */
@Mapper
public interface AlipayConvert {

    AlipayConvert CONVERT = Mappers.getMapper(AlipayConvert.class);

    AliPayConfig convert(AliPayConfigDto in);

    AliPayRecordDto convert(AliPayRecord in);

    AliPayConfig convert(AliPayConfigParam in);

    AliPayConfigDto convert(AliPayConfig in);
}
