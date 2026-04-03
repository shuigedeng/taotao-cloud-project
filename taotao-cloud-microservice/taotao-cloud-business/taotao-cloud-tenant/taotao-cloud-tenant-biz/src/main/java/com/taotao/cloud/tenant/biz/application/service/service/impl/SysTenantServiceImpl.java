package com.taotao.cloud.tenant.biz.application.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.tenant.biz.application.dto.SysTenantDTO;
import com.taotao.cloud.tenant.biz.application.dto.SysTenantQuery;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysTenant;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.SysTenantMapper;
import com.taotao.cloud.tenant.biz.application.service.service.ISysTenantService;
import com.mdframe.forge.starter.core.session.LoginUser;
import com.mdframe.forge.starter.core.session.SessionHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 租户Service实现类
 */
@Service
@RequiredArgsConstructor
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    private final SysTenantMapper tenantMapper;

    @Override
    public IPage<SysTenant> selectTenantPage(SysTenantQuery query) {
        LambdaQueryWrapper<SysTenant> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(query.getTenantName()), SysTenant::getTenantName, query.getTenantName())
                .like(StringUtils.isNotBlank(query.getContactPerson()), SysTenant::getContactPerson, query.getContactPerson())
                .eq(query.getTenantStatus() != null, SysTenant::getTenantStatus, query.getTenantStatus())
                .orderByDesc(SysTenant::getCreateTime);

        Page<SysTenant> page = new Page<>(query.getPageNum(), query.getPageSize());
        return tenantMapper.selectPage(page, wrapper);
    }

    @Override
    public SysTenant selectTenantById(Long id) {
        return tenantMapper.selectById(id);
    }
    
    @Override
    public SysTenant selectUserTenantConfig(Long tenantId) {
        if (tenantId != null) {
            return tenantMapper.selectById(tenantId);
        }
        LoginUser loginUser = SessionHelper.getLoginUser();
        return this.lambdaQuery().eq(SysTenant::getId, loginUser.getTenantId()).one();
    }
    
    @Override
    public boolean insertTenant(SysTenantDTO dto) {
        SysTenant tenant = new SysTenant();
        BeanUtil.copyProperties(dto, tenant);
        return tenantMapper.insert(tenant) > 0;
    }

    @Override
    public boolean updateTenant(SysTenantDTO dto) {
        SysTenant tenant = new SysTenant();
        BeanUtil.copyProperties(dto, tenant);
        return tenantMapper.updateById(tenant) > 0;
    }

    @Override
    public boolean deleteTenantById(Long id) {
        return tenantMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deleteTenantByIds(Long[] ids) {
        return tenantMapper.deleteBatchIds(Arrays.asList(ids)) > 0;
    }
}
