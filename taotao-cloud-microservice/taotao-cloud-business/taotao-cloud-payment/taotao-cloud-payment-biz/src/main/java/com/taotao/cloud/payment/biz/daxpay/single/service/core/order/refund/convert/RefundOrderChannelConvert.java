package com.taotao.cloud.payment.biz.daxpay.single.service.core.order.refund.convert;

import cn.bootx.platform.daxpay.result.order.RefundChannelOrderResult;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.refund.entity.RefundChannelOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.order.refund.RefundChannelOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/17
 */
@Mapper
public interface RefundOrderChannelConvert {
    RefundOrderChannelConvert CONVERT = Mappers.getMapper(RefundOrderChannelConvert.class);


    RefundChannelOrderDto convert(RefundChannelOrder in);

    RefundChannelOrderResult convertResult(RefundChannelOrder in);

}
