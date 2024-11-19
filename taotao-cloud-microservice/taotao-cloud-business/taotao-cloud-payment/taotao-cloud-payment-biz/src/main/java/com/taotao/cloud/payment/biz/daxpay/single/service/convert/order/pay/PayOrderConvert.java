package com.taotao.cloud.payment.biz.daxpay.single.service.convert.order.pay;

import com.taotao.cloud.payment.biz.daxpay.core.result.trade.pay.PayOrderResult;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.pay.PayOrder;
import com.taotao.cloud.payment.biz.daxpay.service.result.order.pay.PayOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付订单
 * @author xxm
 * @since 2024/6/27
 */
@Mapper
public interface PayOrderConvert {
    PayOrderConvert CONVERT = Mappers.getMapper(PayOrderConvert.class);

    PayOrderVo toVo(PayOrder payOrder);

    PayOrderResult toResult(PayOrder payOrder);
}
