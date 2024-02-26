package com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.convert;

import cn.bootx.platform.daxpay.result.order.PayChannelOrderResult;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayChannelOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayOrderExtra;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.order.pay.PayChannelOrderDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.order.pay.PayOrderDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.order.pay.PayOrderExtraDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/9
 */
@Mapper
public interface PayOrderConvert {
    PayOrderConvert CONVERT = Mappers.getMapper(PayOrderConvert.class);

    PayOrderExtraDto convert(PayOrderExtra in);

    PayOrderDto convert(PayOrder in);

    PayChannelOrderDto convert(PayChannelOrder in);

    PayChannelOrderResult convertResult(PayChannelOrder in);
}
