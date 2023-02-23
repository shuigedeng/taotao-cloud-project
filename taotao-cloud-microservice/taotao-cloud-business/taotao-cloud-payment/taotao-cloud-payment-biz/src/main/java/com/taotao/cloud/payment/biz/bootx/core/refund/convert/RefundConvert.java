package com.taotao.cloud.payment.biz.bootx.core.refund.convert;

import cn.bootx.payment.core.refund.entity.RefundRecord;
import cn.bootx.payment.dto.refund.RefundRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
*
* @author xxm
* @date 2022/3/2
*/
@Mapper
public interface RefundConvert {
    RefundConvert CONVERT = Mappers.getMapper(RefundConvert.class);

    RefundRecordDto convert(RefundRecord in);

}
