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

package com.taotao.cloud.stock.biz.domain.tenant.external;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租户注册服务
 *
 * @author shuigedeng
 * @since 2021-05-10
 */
public class TenantRegisterService {

    private TenantRepository tenantRepository;

    private RoleRepository roleRepository;

    private PermissionRepository permissionRepository;

    private UserRepository userRepository;

    public TenantRegisterService(
            TenantRepository tenantRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            UserRepository userRepository) {
        this.tenantRepository = tenantRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    public void registerTenant(
            TenantName tenantName, TenantCode tenantCode, Mobile mobile, Password password, UserName userName) {
        Tenant tenant = new Tenant(tenantCode, tenantName);
        TenantCreateSpecification roleCreateSpecification = new TenantCreateSpecification(tenantRepository);
        roleCreateSpecification.isSatisfiedBy(tenant);
        // 保存租户
        TenantId tenantId = tenantRepository.store(tenant);
        // 保存角色
        Map<String, Object> param = new HashMap<>();
        param.put("permissionLevel", PermissionLevelEnum.TENANT.getValue());
        List<Permission> tenantPermission = permissionRepository.queryList(param);
        List<PermissionId> tenantPermissionId = new ArrayList<>();
        for (Permission permission : tenantPermission) {
            tenantPermissionId.add(permission.getPermissionId());
        }
        Role adminRole =
                new Role(new RoleCode(RoleCode.TENANT_ADMIN), new RoleName(RoleName.TENANT_ADMIN), tenantPermissionId);
        RoleId adminRoleId = roleRepository.store(adminRole);
        // 保存用户
        List<RoleId> roleIdList = new ArrayList<>();
        roleIdList.add(adminRoleId);
        UserFactory userFactory = new UserFactory(userRepository);
        User user =
                userFactory.createUser(mobile, null, password, userName, roleIdList, new TenantId(tenantId.getId()));
        UserId userId = userRepository.store(user);
        tenant = new Tenant(tenantId, tenantCode, tenantName, StatusEnum.ENABLE, userId);
        tenantRepository.store(tenant);
    }
}
