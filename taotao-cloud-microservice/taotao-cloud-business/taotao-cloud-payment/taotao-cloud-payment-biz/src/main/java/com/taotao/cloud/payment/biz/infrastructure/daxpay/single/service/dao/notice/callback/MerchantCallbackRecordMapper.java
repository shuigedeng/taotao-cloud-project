package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.service.dao.notice.callback;

import com.github.yulichang.base.MPJBaseMapper;
import com.taotao.cloud.payment.biz.daxpay.service.entity.notice.callback.MerchantCallbackRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author xxm
 * @since 2024/7/30
 */
@Mapper
public interface MerchantCallbackRecordMapper extends MPJBaseMapper<MerchantCallbackRecord> {
}
