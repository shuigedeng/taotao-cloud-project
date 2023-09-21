/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.stock.biz.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户查询服务实现类
 *
 * @author shuigedeng
 * @since 2021-05-10
 */
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
