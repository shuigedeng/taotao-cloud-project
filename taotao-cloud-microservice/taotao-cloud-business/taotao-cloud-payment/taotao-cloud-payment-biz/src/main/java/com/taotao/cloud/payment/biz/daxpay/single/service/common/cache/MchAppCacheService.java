package com.taotao.cloud.payment.biz.daxpay.single.service.common.cache;

import com.taotao.cloud.payment.biz.daxpay.core.exception.ConfigNotEnableException;
import com.taotao.cloud.payment.biz.daxpay.service.dao.merchant.MchAppManager;
import com.taotao.cloud.payment.biz.daxpay.service.entity.merchant.MchApp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 商户应用缓存服务
 * @author xxm
 * @since 2024/6/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MchAppCacheService {

    private final MchAppManager mchAppManager;

    /**
     * 获取通道配置
     */
    @Cacheable(value = "cache:mchApp", key = "#appId")
    public MchApp get(String appId) {
        return mchAppManager.findByAppId(appId)
                .orElseThrow(() -> new ConfigNotEnableException("未找到指定的应用配置"));
    }

}