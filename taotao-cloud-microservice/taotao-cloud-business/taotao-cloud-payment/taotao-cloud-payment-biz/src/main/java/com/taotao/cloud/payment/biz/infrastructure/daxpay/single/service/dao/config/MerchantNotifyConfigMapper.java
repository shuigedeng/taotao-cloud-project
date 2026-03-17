package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.service.dao.config;

import com.github.yulichang.base.MPJBaseMapper;
import com.taotao.cloud.payment.biz.daxpay.service.entity.config.MerchantNotifyConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author xxm
 * @since 2024/8/2
 */
@Mapper
public interface MerchantNotifyConfigMapper extends MPJBaseMapper<MerchantNotifyConfig> {
}
