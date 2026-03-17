package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.service.dao.config;

import com.github.yulichang.base.MPJBaseMapper;
import com.taotao.cloud.payment.biz.daxpay.service.entity.config.ChannelCashierConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通道收银台配置
 * @author xxm
 * @since 2024/9/28
 */
@Mapper
public interface ChannelCashierConfigMapper extends MPJBaseMapper<ChannelCashierConfig> {
}
