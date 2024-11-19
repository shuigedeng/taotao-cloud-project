package com.taotao.cloud.payment.biz.daxpay.single.service.convert.order.transfer;

import com.taotao.cloud.payment.biz.daxpay.core.result.trade.transfer.TransferOrderResult;
import com.taotao.cloud.payment.biz.daxpay.service.entity.order.transfer.TransferOrder;
import com.taotao.cloud.payment.biz.daxpay.service.result.order.transfer.TransferOrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/7/20
 */
@Mapper
public interface TransferOrderConvert {
    TransferOrderConvert CONVERT = Mappers.getMapper(TransferOrderConvert.class);

    TransferOrderVo toVo(TransferOrder in);

    TransferOrderResult toResult(TransferOrder in);

}
