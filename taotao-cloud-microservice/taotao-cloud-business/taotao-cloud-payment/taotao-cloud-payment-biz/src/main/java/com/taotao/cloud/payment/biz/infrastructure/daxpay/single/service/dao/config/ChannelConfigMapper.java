package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.service.dao.config;

import com.github.yulichang.base.MPJBaseMapper;
import com.taotao.cloud.payment.biz.daxpay.service.entity.config.ChannelConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author xxm
 * @since 2024/6/25
 */
@Mapper
public interface ChannelConfigMapper extends MPJBaseMapper<ChannelConfig> {
}
