package com.taotao.cloud.payment.biz.daxpay.single.service.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import com.taotao.cloud.payment.biz.daxpay.service.entity.config.PlatformConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/7/17
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PlatformConfigManager extends BaseManager<PlatformConfigMapper, PlatformConfig> {
}
