package com.taotao.cloud.payment.biz.daxpay.single.service.convert.order.refund;

import com.taotao.cloud.payment.biz.daxpay.core.result.trade.refund.RefundOrderResult;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.refund.RefundOrder;
import com.taotao.cloud.payment.biz.daxpay.service.result.order.refund.RefundOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/3/2
 */
@Mapper
public interface RefundOrderConvert {

    RefundOrderConvert CONVERT = Mappers.getMapper(RefundOrderConvert.class);

    RefundOrderVo toVo(RefundOrder in);

    RefundOrderResult toResult(RefundOrder in);
}
