package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.service.dao.config;

import com.github.yulichang.base.MPJBaseMapper;
import com.taotao.cloud.payment.biz.daxpay.service.entity.config.PlatformConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author xxm
 * @since 2024/7/17
 */
@Mapper
public interface PlatformConfigMapper extends MPJBaseMapper<PlatformConfig> {
}
