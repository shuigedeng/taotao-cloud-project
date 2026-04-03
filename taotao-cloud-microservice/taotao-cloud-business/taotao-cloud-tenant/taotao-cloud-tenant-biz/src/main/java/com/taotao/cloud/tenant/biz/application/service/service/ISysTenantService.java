package com.taotao.cloud.tenant.biz.application.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.tenant.biz.application.dto.SysTenantDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysTenantQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysTenant;

/**
 * 租户Service接口
 */
public interface ISysTenantService extends IService<SysTenant> {

    /**
     * 分页查询租户列表
     *
     * @param query 查询条件
     * @return 租户分页列表
     */
    IPage<SysTenant> selectTenantPage(SysTenantQuery query);

    /**
     * 根据ID查询租户详情
     *
     * @param id 租户ID
     * @return 租户详情
     */
    SysTenant selectTenantById(Long id);
    
    /**
     * 查询用户当前租户的配置
     * @param tenantId 租户ID
     * @return 租户配置
     */
    SysTenant selectUserTenantConfig(Long tenantId);

    /**
     * 新增租户
     *
     * @param dto 租户信息
     * @return 是否成功
     */
    boolean insertTenant(SysTenantDTO dto);

    /**
     * 修改租户
     *
     * @param dto 租户信息
     * @return 是否成功
     */
    boolean updateTenant(SysTenantDTO dto);

    /**
     * 删除租户
     *
     * @param id 租户ID
     * @return 是否成功
     */
    boolean deleteTenantById(Long id);

    /**
     * 批量删除租户
     *
     * @param ids 租户ID数组
     * @return 是否成功
     */
    boolean deleteTenantByIds(Long[] ids);
}
