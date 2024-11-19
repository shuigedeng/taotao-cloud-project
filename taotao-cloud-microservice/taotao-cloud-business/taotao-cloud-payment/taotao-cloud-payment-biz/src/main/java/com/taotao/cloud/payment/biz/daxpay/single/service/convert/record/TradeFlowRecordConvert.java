package com.taotao.cloud.payment.biz.daxpay.single.service.convert.record;

import com.taotao.cloud.payment.biz.daxpay.service.entity.record.flow.TradeFlowRecord;
import com.taotao.cloud.payment.biz.daxpay.service.result.record.flow.TradeFlowRecordResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/4/21
 */
@Mapper
public interface TradeFlowRecordConvert {
    TradeFlowRecordConvert CONVERT = Mappers.getMapper(TradeFlowRecordConvert.class);

    TradeFlowRecordResult convert(TradeFlowRecord entity);
}
