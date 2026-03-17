package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.service.dao.record.callback;

import com.github.yulichang.base.MPJBaseMapper;
import com.taotao.cloud.payment.biz.daxpay.service.entity.record.callback.TradeCallbackRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author xxm
 * @since 2024/7/22
 */
@Mapper
public interface TradeCallbackRecordMapper extends MPJBaseMapper<TradeCallbackRecord> {
}
