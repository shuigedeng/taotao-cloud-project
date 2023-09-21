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

package com.taotao.cloud.stock.biz.domain.role.model.specification;

/**
 * 角色创建Specification
 *
 * @author shuigedeng
 * @since 2021-02-20
 */
public class RoleCreateSpecification extends AbstractSpecification<Role> {

    private RoleRepository roleRepository;

    public RoleCreateSpecification(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean isSatisfiedBy(Role role) {
        if (role.getRoleCode() != null) {
            Role existRole = roleRepository.find(role.getRoleCode());
            if (existRole != null && !existRole.getRoleId().sameValueAs(role.getRoleId())) {
                throw new IllegalArgumentException("角色编码已存在");
            }
        }
        if (role.getRoleName() != null) {
            Role existRole = roleRepository.find(role.getRoleName());
            if (existRole != null && !existRole.getRoleId().sameValueAs(role.getRoleId())) {
                throw new IllegalArgumentException("角色名称已存在");
            }
        }
        if (role.getRoleCode().isTenantAdmin()) {
            throw new IllegalArgumentException("管理角色无法更新");
        }
        return true;
    }
}
