package com.taotao.cloud.stock.biz.domain.tenant.repository;

import com.taotao.cloud.stock.biz.domain.model.tenant.Tenant;
import com.taotao.cloud.stock.biz.domain.model.tenant.TenantCode;
import com.taotao.cloud.stock.biz.domain.model.tenant.TenantId;
import com.taotao.cloud.stock.biz.domain.model.tenant.TenantName;
import com.taotao.cloud.stock.biz.domain.tenant.model.entity.Tenant;
import com.taotao.cloud.stock.biz.domain.tenant.model.vo.TenantCode;
import com.taotao.cloud.stock.biz.domain.tenant.model.vo.TenantId;
import com.taotao.cloud.stock.biz.domain.tenant.model.vo.TenantName;

/**
 * 租户-Repository接口
 *
 * @author shuigedeng
 * @date 2021-02-14
 **/
public interface TenantRepository {

    /**
     * 通过租户id获取租户
     *
     * @param tenantId
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.tenant.Tenant find(
		    com.taotao.cloud.stock.biz.domain.model.tenant.TenantId tenantId);

    /**
     * 通过租户名称获取租户
     *
     * @param tenantName
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.tenant.Tenant find(TenantName tenantName);

    /**
     * 通过租户编码获取租户
     *
     * @param tenantCode
     * @return
     */
    com.taotao.cloud.stock.biz.domain.model.tenant.Tenant find(TenantCode tenantCode);

    /**
     * 保存
     *
     * @param tenant
     */
    TenantId store(Tenant tenant);

}
