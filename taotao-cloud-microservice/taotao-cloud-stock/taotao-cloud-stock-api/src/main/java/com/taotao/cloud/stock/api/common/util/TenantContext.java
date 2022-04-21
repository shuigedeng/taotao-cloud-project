package com.taotao.cloud.stock.api.common.util;

import lombok.experimental.UtilityClass;

/**
 * 多租户 tenant_id存储器
 */
@UtilityClass
public class TenantContext {

    /**
     * 支持父子线程之间的数据传递
     */
    private final ThreadLocal<String> THREAD_LOCAL_TENANT = new ThreadLocal<>();

    /**
     * 设置租户ID
     *
     * @param tenantId 租户ID
     */
    public void setTenantId(String tenantId) {
        THREAD_LOCAL_TENANT.set(tenantId);
    }

    /**
     * 获取TTL中的租户ID
     *
     * @return String
     */
    public String getTenantId() {
        return THREAD_LOCAL_TENANT.get();
    }

    /**
     * 清除tenantId
     */
    public void clear() {
        THREAD_LOCAL_TENANT.remove();
    }
}
