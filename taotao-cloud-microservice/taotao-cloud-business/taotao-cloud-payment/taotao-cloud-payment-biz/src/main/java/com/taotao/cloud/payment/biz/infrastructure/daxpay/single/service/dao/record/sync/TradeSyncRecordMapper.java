package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.service.dao.record.sync;

import com.github.yulichang.base.MPJBaseMapper;
import com.taotao.cloud.payment.biz.daxpay.service.entity.record.sync.TradeSyncRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付同步记录
 * @author xxm
 * @since 2023/7/14
 */
@Mapper
public interface TradeSyncRecordMapper extends MPJBaseMapper<TradeSyncRecord> {
}
