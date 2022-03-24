package com.taotao.cloud.stock.biz.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xtoon.cloud.common.mybatis.util.Page;
import com.xtoon.cloud.common.mybatis.util.PageAssembler;
import com.xtoon.cloud.common.mybatis.util.Query;
import com.xtoon.cloud.sys.application.PermissionQueryService;
import com.xtoon.cloud.sys.application.UserQueryService;
import com.xtoon.cloud.sys.application.assembler.UserDTOAssembler;
import com.xtoon.cloud.sys.application.dto.TenantDTO;
import com.xtoon.cloud.sys.application.dto.UserDTO;
import com.xtoon.cloud.sys.domain.model.user.User;
import com.xtoon.cloud.sys.domain.model.user.UserId;
import com.xtoon.cloud.sys.domain.model.user.UserRepository;
import com.xtoon.cloud.sys.infrastructure.persistence.entity.SysTenantDO;
import com.xtoon.cloud.sys.infrastructure.persistence.entity.SysUserDO;
import com.xtoon.cloud.sys.infrastructure.persistence.mapper.SysPermissionMapper;
import com.xtoon.cloud.sys.infrastructure.persistence.mapper.SysTenantMapper;
import com.xtoon.cloud.sys.infrastructure.persistence.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户查询服务实现类
 *
 * @author shuigedeng
 * @date 2021-05-10
 **/
@Service
public class UserQueryServiceImpl implements UserQueryService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SysTenantMapper sysTenantMapper;

    @Autowired
    private PermissionQueryService permissionQueryService;

    @Override
    public Page queryPage(Map<String, Object> params) {
        IPage<SysUserDO> page = sysUserMapper.queryPage(new Query().getPage(params), params);
        return PageAssembler.toPage(page);
    }

    @Override
    public UserDTO find(String userId) {
        User user = userRepository.find(new UserId(userId));
        UserDTO userDTO = UserDTOAssembler.fromUser(user);
        SysTenantDO tenantDO = sysTenantMapper.selectById(user.getTenantId());
        userDTO.setTenantName(tenantDO.getTenantName());
        userDTO.setPermissionCodes(permissionQueryService.getPermissionCodes(userId));
        userDTO.setPermissionIds(permissionQueryService.getPermissionIds(userId));
        userDTO.setTenants(getUserTenants(userId));
        return userDTO;
    }

    /**
     * 获取用户关联的租户
     *
     * @param userId
     * @return
     */
    private List<TenantDTO> getUserTenants(String userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        List<SysUserDO> sysUserDOList = sysUserMapper.queryUserNoTenant(params);
        List<TenantDTO> tenants = new ArrayList<>();
        for (SysUserDO sysUserDO : sysUserDOList) {
            TenantDTO tenantDTO = new TenantDTO();
            tenantDTO.setTenantId(sysUserDO.getTenantId());
            tenantDTO.setTenantCode(sysUserDO.getTenantCode());
            tenantDTO.setTenantName(sysUserDO.getTenantName());
            tenants.add(tenantDTO);
        }
        return tenants;
    }
}
